import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
  getUnderStock(): Observable<Product[]> { return this.http.get<Product[]>(`${this.productApi}/under-stock`); }
}
