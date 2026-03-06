import { Component, signal } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {DataService} from './dataService';
@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  imports: [
    RouterOutlet,
  ],
  styleUrl: './app.css'
})
export class App {
  constructor(private router: Router) {
    // Dacă suntem pe ruta de login, ștergem orice urmă de sesiune veche
    this.router.events.subscribe(() => {
      if (this.router.url === '/login') {
        localStorage.removeItem('authToken'); // sau cum l-ai numit
      }
    });
  }
  protected readonly title = signal('frontend-app');
}
