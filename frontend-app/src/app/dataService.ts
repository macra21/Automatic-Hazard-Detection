import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  // Prefixul definit de colegul tău în @RequestMapping
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(credentials: any): Observable<any> {
    // Aceasta va apela http://localhost:8080/api/auth/login
    return this.http.post(`${this.baseUrl}/login`, credentials);
  }

  register(user: any): Observable<any> {
    // Aceasta va apela http://localhost:8080/api/auth/register
    return this.http.post(`${this.baseUrl}/register`, user);
  }
}
