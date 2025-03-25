import {Injectable, OnInit} from "@angular/core";
import { Observable, BehaviorSubject } from "rxjs";

import { JwtService } from "./jwt.service";
import { map, distinctUntilChanged, tap, shareReplay } from "rxjs/operators";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import {User} from './user.model';

@Injectable({ providedIn: "root" })
export class UserService {
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromToken());
  public currentUser = this.currentUserSubject
    .asObservable()
    .pipe(distinctUntilChanged());

  public isAuthenticated = this.currentUser.pipe(map((user) => !!user));

  constructor(
    private readonly http: HttpClient,
    private readonly jwtService: JwtService,
    private readonly router: Router,
  ) {}

  login(credentials: {
    login: string;
    password: string;
  }): Observable<{ user: User }> {
    return this.http
      .post<{ token: string }>("/users/login", credentials)
      .pipe(map(({token}) => {
        const login = credentials.login;
        return {user: {login, token}};
      }))
      .pipe(tap(({user}) => this.setAuth(user)));
  }

  register(credentials: {
    login: string;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    password: string
  }): Observable<{ login: string }> {
    return this.http
      .post<{ login: string }>("/users/register", credentials)
      .pipe(
        tap(({login}) => {
          console.log(`User ${login} registered`);
          this.router.navigate(["/login"]);
        })
      );
  }

  logout(): void {
    this.purgeAuth();
    void this.router.navigate(["/"]);
  }

  getCurrentUser(): Observable<{ user: User }> {
    return this.http.get<{ user: User }>("/user").pipe(
      tap({
        next: ({ user }) => this.setAuth(user),
        error: () => this.purgeAuth(),
      }),
      shareReplay(1),
    );
  }

  update(user: Partial<User>): Observable<{ user: User }> {
    return this.http.put<{ user: User }>("/user", { user }).pipe(
      tap(({ user }) => {
        this.currentUserSubject.next(user);
      }),
    );
  }

  setAuth(user: User): void {
    this.jwtService.saveToken(user);
    this.currentUserSubject.next(user);
  }

  purgeAuth(): void {
    this.jwtService.destroyToken();
    this.currentUserSubject.next(null);
  }

  private getUserFromToken(): User | null {
    const user = window.localStorage["user"];
    if (user) {
      const token = user.token;
      const login = user.login;
      return { token, login };
    }
    return null;
  }
}
