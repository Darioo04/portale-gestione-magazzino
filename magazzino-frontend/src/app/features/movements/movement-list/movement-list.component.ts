import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MovementService } from '../../../core/services/movement.service';
import { Movement } from '../../../core/models/models';

@Component({
  selector: 'app-movement-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <div class="px-4 py-2">
      <div class="flex justify-between items-center mb-6 border-b border-gray-800 pb-4">
        <h1 class="text-3xl font-light text-white mb-0">Movimenti Storico</h1>
        <div class="space-x-4">
          <button mat-flat-button color="accent">
            <mat-icon class="mr-2">upload</mat-icon> Carico (Entrata)
          </button>
          <button mat-flat-button color="warn">
            <mat-icon class="mr-2">download</mat-icon> Scarico (Uscita)
          </button>
        </div>
      </div>

      <mat-card class="bg-[#1e1e1e] border border-gray-800 shadow-md">
        <mat-card-content class="p-0">
          <div class="overflow-x-auto">
             <table mat-table [dataSource]="movements" class="w-full bg-transparent min-w-[800px]">
               
               <ng-container matColumnDef="id">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4 w-16"> ID </th>
                 <td mat-cell *matCellDef="let element" class="p-4 text-gray-400"> {{element.id}} </td>
               </ng-container>

               <ng-container matColumnDef="date">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> Data Movimento </th>
                 <td mat-cell *matCellDef="let element" class="p-4 text-white"> {{element.dataMovimento | date:'short'}} </td>
               </ng-container>

               <ng-container matColumnDef="quantity">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4 text-right"> Q.tà </th>
                 <td mat-cell *matCellDef="let element" class="p-4 font-bold text-right" [ngClass]="element.quantity > 0 ? 'text-green-500' : 'text-red-500'"> 
                   {{element.quantity > 0 ? '+' : ''}}{{element.quantity}} 
                 </td>
               </ng-container>

               <ng-container matColumnDef="productId">
                 <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> ID Prodotto </th>
                 <td mat-cell *matCellDef="let element" class="p-4 text-blue-400"> #{{element.productId}} </td>
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
export class MovementListComponent implements OnInit {
  movements: Movement[] = [];
  displayedColumns: string[] = ['id', 'date', 'quantity', 'productId'];

  constructor(private movementService: MovementService) {}

  ngOnInit() {
    // Note: To make the frontend completely standalone without mocking the backend data manually here,
    // we would load all movements. Since the movements endpoint requires specific parameters like byUser or DateRange,
    // we will fetch movements by User 1 (Admin) for this showcase view, representing the initial table entries.
    this.movementService.getByUser(1).subscribe(data => {
      this.movements = data;
    });
  }
}
