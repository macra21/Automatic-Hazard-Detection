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
      next: (response:any) => {
        console.log('Succes:', response);
        this.router.navigate(['/page']);
      },
      error: (err:any) => {
        console.error('Eroare la login:', err);
        // Dacă serverul e picat, err.error poate fi null, deci punem un mesaj de rezervă
        this.errorMessage = typeof err.error === 'string' ? err.error : 'Eroare conexiune server (Java)';
      }
    });
  }
}
