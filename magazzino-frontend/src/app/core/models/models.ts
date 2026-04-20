export interface LoginRequest {
  username?: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
}

export interface Category {
  id?: number;
  name: string;
}

export interface ProductType {
  eanCode: string;
  name: string;
  brand: string;
  price: number;
  stockThreshold: number;
  categoryId: number;
}

export interface LocationModel {
  area: string;
  aisle: number;
  shelf: number;
}

export interface Product {
  id?: number;
  eanCode: string;
  quantity: number;
  expirationDate?: string;
  batch: string;
  locationArea: string;
  locationAisle: number;
  locationShelf: number;
}

export interface Movement {
  id?: number;
  dataMovimento?: string;
  quantity: number;
  productId: number;
  userId: number;
}

export interface User {
  id?: number;
  username: string;
  ruolo?: Role;
  persona?: Person;
}

export interface Role {
  id: number;
  nome: string;
}

export interface Person {
  id?: number;
  nome: string;
  cognome: string;
  email: string;
  telefono: string;
}
