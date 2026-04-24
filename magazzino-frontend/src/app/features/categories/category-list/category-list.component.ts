import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CategoryService } from '../../../core/services/category.service';
import { Category } from '../../../core/models/models';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  template: `
    <div class="px-4 py-2">
      <div class="flex justify-between items-center mb-6 border-b border-gray-800 pb-4">
        <h1 class="text-3xl font-light text-white mb-0">Categorie</h1>
        <button mat-flat-button color="primary">
          <mat-icon class="mr-2">add</mat-icon> Nuova Categoria
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
          <table mat-table [dataSource]="categories" class="w-full bg-transparent">

            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let element" class="w-16"> {{element.id}} </td>
            </ng-container>

            <ng-container matColumnDef="name">
              <th mat-header-cell *matHeaderCellDef> Nome </th>
              <td mat-cell *matCellDef="let element" class="font-medium text-white"> {{element.name}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
               <th mat-header-cell *matHeaderCellDef class="text-right"> Azioni </th>
               <td mat-cell *matCellDef="let element" class="text-right">
                  <button mat-icon-button aria-label="Modifica"><mat-icon>edit</mat-icon></button>
                  <button mat-icon-button color="warn" aria-label="Elimina"><mat-icon>delete</mat-icon></button>
               </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell p-8 text-center text-gray-500" [attr.colspan]="displayedColumns.length">
                Nessuna categoria trovata.
              </td>
            </tr>
          </table>
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
export class CategoryListComponent implements OnInit {
  categories: Category[] = [];
  displayedColumns: string[] = ['id', 'name', 'actions'];
  loading = false;
  error = '';

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.loading = true;
    this.error = '';
    this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Impossibile caricare le categorie. Verifica la connessione al server.';
        this.loading = false;
        console.error('Errore caricamento categorie:', err);
      }
    });
  }
}
