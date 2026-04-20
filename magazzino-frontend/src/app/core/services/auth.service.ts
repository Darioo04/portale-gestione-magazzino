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
    return !!this.getToken();
  }

  private checkToken() {
    const token = this.getToken();
    if (token) {
      try {
        const decoded = jwtDecode<any>(token);
        this.currenUserSubject.next(decoded);
      } catch(e) {
        this.logout();
      }
    }
  }
}
