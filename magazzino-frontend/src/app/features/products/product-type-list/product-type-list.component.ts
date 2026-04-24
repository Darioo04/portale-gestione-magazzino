import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductService } from '../../../core/services/product.service';
import { ProductType } from '../../../core/models/models';

@Component({
  selector: 'app-product-type-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  template: `
    <div class="px-4 py-2">
      <div class="flex justify-between items-center mb-6 border-b border-gray-800 pb-4">
        <h1 class="text-3xl font-light text-white mb-0">Tipi Prodotto</h1>
        <button mat-flat-button color="primary">
          <mat-icon class="mr-2">add</mat-icon> Nuovo Tipo Prodotto
        </button>
      </div>

      <!-- Loading -->
      <div *ngIf="loading" class="flex justify-center items-center py-16">
        <mat-spinner diameter="48"></mat-spinner>
      </div>

      <!-- Errore -->
      <div *ngIf="error && !loading" class="bg-red-500/10 border border-red-500/30 rounded-lg p-4 flex items-center gap-3 mb-4">
        <mat-icon class="text-red-400 flex-shrink-0">error_outline</mat-icon>
        <p class="text-red-400 text-sm m-0">{{ error }}</p>
      </div>

      <!-- Tabella -->
      <mat-card *ngIf="!loading" class="border border-gray-800 shadow-md">
        <mat-card-content class="p-0">
          <div class="overflow-x-auto">
             <table mat-table [dataSource]="productTypes" class="w-full bg-transparent min-w-[700px]">

               <ng-container matColumnDef="eanCode">
                 <th mat-header-cell *matHeaderCellDef> EAN </th>
                 <td mat-cell *matCellDef="let element" class="font-mono"> {{element.eanCode}} </td>
               </ng-container>

               <ng-container matColumnDef="name">
                 <th mat-header-cell *matHeaderCellDef> Nome </th>
                 <td mat-cell *matCellDef="let element" class="font-medium text-white"> {{element.name}} </td>
               </ng-container>

               <ng-container matColumnDef="brand">
                 <th mat-header-cell *matHeaderCellDef> Marca </th>
                 <td mat-cell *matCellDef="let element"> {{element.brand}} </td>
               </ng-container>

               <ng-container matColumnDef="price">
                 <th mat-header-cell *matHeaderCellDef> Prezzo </th>
                 <td mat-cell *matCellDef="let element" class="text-blue-400 font-semibold"> €{{element.price | number:'1.2-2'}} </td>
               </ng-container>

               <ng-container matColumnDef="stockThreshold">
                 <th mat-header-cell *matHeaderCellDef> Soglia Scorta </th>
                 <td mat-cell *matCellDef="let element" class="text-orange-400"> {{element.stockThreshold}} </td>
               </ng-container>

               <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
               <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

               <!-- Riga "nessun dato" -->
               <tr class="mat-row" *matNoDataRow>
                 <td class="mat-cell p-8 text-center text-gray-500" [attr.colspan]="displayedColumns.length">
                   Nessun tipo prodotto trovato.
                 </td>
               </tr>
             </table>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    ::ng-deep .mat-mdc-table { background: transparent !important; }
    ::ng-deep .mat-mdc-header-row { height: 48px !important; }
    ::ng-deep .mat-mdc-row { height: 52px !important; }
  `]
})
export class ProductTypeListComponent implements OnInit {
  productTypes: ProductType[] = [];
  displayedColumns: string[] = ['eanCode', 'name', 'brand', 'price', 'stockThreshold'];
  loading = false;
  error = '';

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.loading = true;
    this.error = '';
    this.productService.getAllTypes().subscribe({
      next: (data) => {
        this.productTypes = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Impossibile caricare i tipi prodotto. Verifica la connessione al server.';
        this.loading = false;
        console.error('Errore caricamento prodotti:', err);
      }
    });
  }
}
