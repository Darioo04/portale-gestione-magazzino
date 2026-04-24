import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movement } from '../models/models';

@Injectable({ providedIn: 'root' })
export class MovementService {
  private apiUrl = 'http://localhost:8080/api/movements';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Movement[]> {
    return this.http.get<Movement[]>(this.apiUrl);
  }

  load(movement: Movement): Observable<Movement> {
    return this.http.post<Movement>(`${this.apiUrl}/load`, movement);
  }

  unload(movement: Movement): Observable<Movement> {
    return this.http.post<Movement>(`${this.apiUrl}/unload`, movement);
  }

  getByUser(userId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/user/${userId}`);
  }

  getByProduct(productId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/product/${productId}`);
  }
}
