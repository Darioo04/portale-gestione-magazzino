import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse, LoginRequest, User } from '../models/models';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  
  private currenUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currenUserSubject.asObservable();

  constructor(private http: HttpClient) { 
    this.checkToken();
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if(response && response.token) {
          localStorage.setItem('token', response.token);
          this.checkToken();
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currenUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const decoded = jwtDecode<any>(token);
      // Verifica scadenza: exp è in secondi Unix
      if (decoded.exp && decoded.exp * 1000 < Date.now()) {
        this.logout();
        return false;
      }
      return true;
    } catch {
      this.logout();
      return false;
    }
  }

  private checkToken() {
    const token = this.getToken();
    if (token) {
      try {
        const decoded = jwtDecode<any>(token);
        // Rimuovi token scaduto al riavvio dell'app
        if (decoded.exp && decoded.exp * 1000 < Date.now()) {
          this.logout();
          return;
        }
        this.currenUserSubject.next(decoded);
      } catch(e) {
        this.logout();
      }
    }
  }
}
