import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
// Adaugă ReactiveFormsModule aici
import { Router } from '@angular/router';
@Component({
  selector: 'app-login-component',
  standalone: true,
  imports : [ReactiveFormsModule],
  templateUrl: './login-component.html',
  styleUrls: ['./login-component.css'],
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });
  constructor(private router: Router) {}
  login() {
    //CALL API with username and password
    if (this.loginForm.invalid) return;

    alert('Calling backend to login');
    this.router.navigate(['/map'])
  }
}
