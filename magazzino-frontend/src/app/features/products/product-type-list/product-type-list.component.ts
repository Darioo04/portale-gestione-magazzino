import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProductService } from '../../../core/services/product.service';
import { ProductType } from '../../../core/models/models';

@Component({
  selector: 'app-product-type-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <div class="px-4 py-2">
      <div class="flex justify-between items-center mb-6 border-b border-gray-800 pb-4">
        <h1 class="text-3xl font-light text-white mb-0">Tipi Prodotto</h1>
        <button mat-flat-button color="primary">
          <mat-icon class="mr-2">add</mat-icon> Nuovo Tipo Prodotto
        </button>
      </div>

      <mat-card class="bg-[#1e1e1e] border border-gray-800 shadow-md">
        <mat-card-content class="p-0">
          <div class="overflow-x-auto">
             <table mat-table [dataSource]="productTypes" class="w-full bg-transparent min-w-[800px]">
               
               <ng-container matColumnDef="eanCode">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> EAN </th>
                 <td mat-cell *matCellDef="let element" class="p-4 font-mono text-gray-400"> {{element.eanCode}} </td>
               </ng-container>

               <ng-container matColumnDef="name">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> Nome </th>
                 <td mat-cell *matCellDef="let element" class="p-4 font-medium text-white"> {{element.name}} </td>
               </ng-container>

               <ng-container matColumnDef="brand">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> Marca </th>
                 <td mat-cell *matCellDef="let element" class="p-4 text-gray-300"> {{element.brand}} </td>
               </ng-container>

               <ng-container matColumnDef="price">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> Prezzo </th>
                 <td mat-cell *matCellDef="let element" class="p-4 text-blue-400"> €{{element.price}} </td>
               </ng-container>

               <ng-container matColumnDef="stockThreshold">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> Soglia Scorta </th>
                 <td mat-cell *matCellDef="let element" class="p-4 text-orange-400"> {{element.stockThreshold}} </td>
               </ng-container>

               <tr mat-header-row *matHeaderRowDef="displayedColumns" class="bg-gray-800/50 border-b border-gray-800"></tr>
               <tr mat-row *matRowDef="let row; columns: displayedColumns;" class="border-b border-gray-800 hover:bg-white/5 transition-colors"></tr>
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

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.getAllTypes().subscribe(data => {
      this.productTypes = data;
    });
  }
}
