import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CategoryService } from '../../../core/services/category.service';
import { Category } from '../../../core/models/models';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <div class="px-4 py-2">
      <div class="flex justify-between items-center mb-6 border-b border-gray-800 pb-4">
        <h1 class="text-3xl font-light text-white mb-0">Categorie</h1>
        <button mat-flat-button color="primary">
          <mat-icon class="mr-2">add</mat-icon> Nuova Categoria
        </button>
      </div>

      <mat-card class="bg-[#1e1e1e] border border-gray-800 shadow-md">
        <mat-card-content class="p-0">
          <table mat-table [dataSource]="categories" class="w-full bg-transparent">
            
            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> ID </th>
              <td mat-cell *matCellDef="let element" class="p-4 text-gray-400"> {{element.id}} </td>
            </ng-container>

            <ng-container matColumnDef="name">
              <th mat-header-cell *matHeaderCellDef class="text-gray-300 font-semibold p-4"> Nome </th>
              <td mat-cell *matCellDef="let element" class="p-4 font-medium text-white"> {{element.name}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
               <th mat-header-cell *matHeaderCellDef class="text-right text-gray-300 font-semibold p-4"> Azioni </th>
               <td mat-cell *matCellDef="let element" class="p-4 text-right">
                  <button mat-icon-button class="text-gray-400 hover:text-blue-400"><mat-icon>edit</mat-icon></button>
                  <button mat-icon-button color="warn"><mat-icon>delete</mat-icon></button>
               </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns" class="bg-gray-800/50 border-b border-gray-800"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;" class="border-b border-gray-800 hover:bg-white/5 transition-colors"></tr>
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

  constructor(private categoryService: CategoryService) {}

  ngOnInit() {
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    });
  }
}
