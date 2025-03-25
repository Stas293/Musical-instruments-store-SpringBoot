import {Component} from '@angular/core';
import {HeaderComponent} from './layout/header/header.component';
import {FooterComponent} from './layout/footer/footer.component';
import {RouterOutlet} from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [
    HeaderComponent,
    FooterComponent,
    RouterOutlet
  ],
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Musical Instruments Store';
}
