import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// 1. Definește enum-ul UserType, bazat pe UserType din Java
// Asigură-te că valorile de aici corespund exact cu cele din UserType.java
export enum UserType {
  ADMIN = 'ADMIN',
  REGULAR = 'REGULAR',
  // Adaugă aici alte tipuri de utilizatori dacă există în backend-ul tău Java
}

// 2. Definește interfața User, bazată pe clasa User din Java
export interface User {
  ID?: number; // ID-ul este opțional deoarece poate fi generat de backend la creare
  email: string;
  password: string;
  username: string;
  type: UserType; // Folosește enum-ul UserType definit mai sus
}

@Injectable({
  providedIn: 'root' // Acest serviciu va fi disponibil la nivel global în aplicație
})
export class UserService {

  // URL-ul de bază al API-ului tău Spring Boot pentru resursa User
  // Asigură-te că acesta este URL-ul corect al backend-ului tău!
  // Exemplu: 'http://localhost:8080/api/users' dacă Spring rulează pe portul 8080
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  /**
   * Prelucrează toți utilizatorii de la backend.
   * @returns Un Observable care emite un array de obiecte User.
   */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  /**
   * Prelucrează un singur utilizator după ID-ul său.
   * @param id ID-ul utilizatorului de preluat.
   * @returns Un Observable care emite un obiect User.
   */
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  /**
   * Creează un utilizator nou în backend.
   * ID-ul nu este trimis, deoarece backend-ul îl va genera.
   * @param user Obiectul User de creat (fără ID).
   * @returns Un Observable care emite obiectul User creat (cu ID-ul generat).
   */
  createUser(user: User): Observable<User> {
    // Destructurăm user pentru a exclude ID-ul, deoarece backend-ul îl va genera
    const { ID, ...userWithoutId } = user;
    return this.http.post<User>(this.apiUrl, userWithoutId);
  }

  /**
   * Actualizează un utilizator existent în backend.
   * @param user Obiectul User cu datele actualizate (trebuie să includă ID-ul).
   * @returns Un Observable care emite obiectul User actualizat.
   */
  updateUser(user: User): Observable<User> {
    // Asigură-te că ID-ul utilizatorului este prezent pentru a ști ce utilizator să actualizezi
    if (user.ID === undefined || user.ID === null) {
      throw new Error('User ID is required for update operation.');
    }
    return this.http.put<User>(`${this.apiUrl}/${user.ID}`, user);
  }

  /**
   * Șterge un utilizator din backend după ID-ul său.
   * @param id ID-ul utilizatorului de șters.
   * @returns Un Observable care emite void la succes.
   */
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
