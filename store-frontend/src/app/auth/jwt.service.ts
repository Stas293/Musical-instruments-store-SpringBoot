import { Injectable } from "@angular/core";
import {User} from './user.model';

@Injectable({ providedIn: "root" })
export class JwtService {
  getToken(): string {
    return window.localStorage["user"].token;
  }

  getLogin(): string {
    return window.localStorage["user"].login;
  }

  saveToken(user: User): void {
    window.localStorage["user"] = user;
  }

  destroyToken(): void {
    window.localStorage.removeItem("user");
  }
}
