import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product, ProductType } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private productApi = 'http://localhost:8080/api/products';
  private productTypeApi = 'http://localhost:8080/api/product-types';

  constructor(private http: HttpClient) {}

  // Product Types
  getAllTypes(): Observable<ProductType[]> { return this.http.get<ProductType[]>(this.productTypeApi); }
  createType(type: ProductType): Observable<ProductType> { return this.http.post<ProductType>(this.productTypeApi, type); }
  deleteType(ean: string): Observable<void> { return this.http.delete<void>(`${this.productTypeApi}/${ean}`); }

  // Physical Products
  getAllProducts(): Observable<Product[]> { return this.http.get<Product[]>(this.productApi); }
  createProduct(product: Product): Observable<Product> { return this.http.post<Product>(this.productApi, product); }
  deleteProduct(id: number): Observable<void> { return this.http.delete<void>(`${this.productApi}/${id}`); }
  
  // L'endpoint under-stock non esiste nel backend. Nel Magazzino vero i prodotti sotto scorta si controllano 
  // in base al type, ma la dashboard al momento accetta qualsiasi prodotto. Come workaround temporaneo
  // filtriamo tutti i prodotti. Se avessimo stockThreshold nel Product DTO lo useremmo.
  // Dato che non c'è, restituiamo i prodotti con quantity < 10 di default, oppure un array vuoto
  // in modo che l'app funzioni finchè l'endpoint non viene aggiunto al backend.
  getUnderStock(): Observable<Product[]> { 
    return this.http.get<Product[]>(this.productApi).pipe(
      map(products => products.filter(p => p.quantity < 10))
    );
  }
}
