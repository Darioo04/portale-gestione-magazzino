import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../core/services/product.service';
import { Product } from '../../core/models/models';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatProgressSpinnerModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  underStockProducts$: Observable<Product[]> | undefined;
  underStockError = false;

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.underStockProducts$ = this.productService.getUnderStock().pipe(
      catchError(err => {
        this.underStockError = true;
        console.error('Errore sotto-scorta:', err);
        return of([]);
      })
    );
  }
}
