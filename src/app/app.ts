import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
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
  protected readonly title = signal('frontend-app');
}
