import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MovementService } from '../../../core/services/movement.service';
import { Movement } from '../../../core/models/models';

@Component({
  selector: 'app-movement-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  template: `
    <div class="px-4 py-2">
      <div class="flex justify-between items-center mb-6 border-b border-gray-800 pb-4">
        <h1 class="text-3xl font-light text-white mb-0">Movimenti Storico</h1>
        <div class="flex gap-3 flex-wrap">
          <button mat-flat-button color="accent">
            <mat-icon class="mr-1">upload</mat-icon> Carico
          </button>
          <button mat-flat-button color="warn">
            <mat-icon class="mr-1">download</mat-icon> Scarico
          </button>
        </div>
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
             <table mat-table [dataSource]="movements" class="w-full bg-transparent min-w-[600px]">

               <ng-container matColumnDef="id">
                 <th mat-header-cell *matHeaderCellDef class="w-16"> ID </th>
                 <td mat-cell *matCellDef="let element"> {{element.id}} </td>
               </ng-container>

               <ng-container matColumnDef="date">
                 <th mat-header-cell *matHeaderCellDef> Data Movimento </th>
                 <td mat-cell *matCellDef="let element"> {{element.movementDate | date:'dd/MM/yyyy HH:mm'}} </td>
               </ng-container>

               <ng-container matColumnDef="productInfo">
                 <th mat-header-cell *matHeaderCellDef> Prodotto </th>
                 <td mat-cell *matCellDef="let element"> 
                   <div class="flex flex-col">
                     <span class="text-white">{{element.productName}}</span>
                     <span class="text-xs text-gray-500 font-mono">{{element.productEanCode}}</span>
                   </div>
                 </td>
               </ng-container>

               <ng-container matColumnDef="quantity">
                 <th mat-header-cell *matHeaderCellDef class="text-right"> Q.tà </th>
                 <td mat-cell *matCellDef="let element" class="text-right font-bold"
                     [class.text-green-500]="element.quantity > 0"
                     [class.text-red-500]="element.quantity < 0">
                   {{element.quantity > 0 ? '+' : ''}}{{element.quantity}}
                 </td>
               </ng-container>

               <ng-container matColumnDef="user">
                 <th mat-header-cell *matHeaderCellDef> Utente </th>
                 <td mat-cell *matCellDef="let element" class="text-blue-400"> {{element.userFirstName}} {{element.userLastName}} </td>
               </ng-container>

               <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
               <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

               <tr class="mat-row" *matNoDataRow>
                 <td class="mat-cell p-8 text-center text-gray-500" [attr.colspan]="displayedColumns.length">
                   Nessun movimento trovato.
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
export class MovementListComponent implements OnInit {
  movements: Movement[] = [];
  displayedColumns: string[] = ['id', 'date', 'productInfo', 'quantity', 'user'];
  loading = false;
  error = '';

  constructor(private movementService: MovementService) {}

  ngOnInit() {
    this.loading = true;
    this.error = '';
    this.movementService.getAll().subscribe({      next: (data) => {
        this.movements = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Impossibile caricare i movimenti. Verifica la connessione al server.';
        this.loading = false;
        console.error('Errore caricamento movimenti:', err);
      }
    });
  }
}
