import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {IsAuthenticatedDirective} from '../../auth/is-authenticated.directive';
import {UserService} from '../../auth/user.service';

@Component({
  selector: 'app-header',
  imports: [
    RouterLink,
    IsAuthenticatedDirective
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor(private userService: UserService) {
  }

  logout(): void {
    this.userService.logout();
  }
}
