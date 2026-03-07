import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials).pipe(
      tap((response: any) => {
        // SETĂM ÎN SESSION STORAGE
        sessionStorage.setItem('app_auth_token', 'true');
        // Save user details to session storage
        if (response) {
            sessionStorage.setItem('currentUser', JSON.stringify(response));
        }
        console.log('Sesiune temporară creată în SessionStorage');
      })
    );
  }

  // Verificăm TOT în SESSION STORAGE
  isLoggedIn(): boolean {
    const token = sessionStorage.getItem('app_auth_token');
    return token === 'true';
  }

  getCurrentUser(): any {
      const userStr = sessionStorage.getItem('currentUser');
      return userStr ? JSON.parse(userStr) : null;
  }

  // Metodă pentru Logout
  logout(): void {
    sessionStorage.clear(); // Șterge sesiunea curentă
    // Opțional, poți lăsa și localStorage.clear() dacă vrei să fii paranoic,
    // dar sessionStorage.clear() e cel care contează acum.
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }
}
