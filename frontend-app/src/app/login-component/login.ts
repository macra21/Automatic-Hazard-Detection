import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DataService } from '../dataService';
import { CommonModule } from '@angular/common'; // Obligatoriu pentru *ngIf in standalone

@Component({
  selector: 'app-login-component',
  standalone: true,
  // Asigură-te că ai ambele module aici:
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login {
  // Folosim non-null assertion (!) sau valori default ca să nu urle TS
  loginForm = new FormGroup({
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  errorMessage: string = '';

  constructor(
    private router: Router,
    private dataService: DataService
  ) {}

  login(): void {
    if (this.loginForm.invalid) {
      return;
    }

    // Luăm valorile sigure
    const credentials = this.loginForm.getRawValue();

    this.dataService.login(credentials).subscribe({
      next: (response: any) => {
        console.log('Login reușit, pregătim redirect...');
        // Ne asigurăm că am salvat (deși tap-ul din service a făcut-o deja)
        sessionStorage.setItem('app_auth_token', 'true');

        const userRole = response.type // sau response.type, în funcție de ce îți vine de la backend

        if (userRole === 'ATC') {
          this.router.navigate(['/operatorPage']);
        } else if (userRole === 'CLEANUP') {
          this.router.navigate(['/maintenancePage']); // pune aici ruta corectă pentru cleanup
        } else {
          console.error('Rol invalid:', userRole);
        }

        /*// Navigăm la /page
        this.router.navigate(['/operatorPage']).then(success => {
          if (!success) {
            console.error('Navigarea a fost respinsă de Guard!');
          }
        });
         */
      },
      error: (err: any) => { console.log(err.error.message) }
    });
  }
}
