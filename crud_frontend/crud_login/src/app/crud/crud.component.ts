import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { CrudloginService, CrudItem, UserRole } from '../services/crudlogin.service';

@Component({
  selector: 'app-crud',
  standalone: false,
  templateUrl: './crud.component.html',
  styleUrls: ['./crud.component.css']
})
export class CrudComponent implements OnInit {

  username = '';
  role: string = 'user';
  displayedColumns: string[] = [
    'name', 'fatherName', 'age', 'email', 'dateOfBirth', 'designation', 'action'
  ];

  dataSource = new MatTableDataSource<CrudItem>();

  showModal = false;
  selected: CrudItem = {} as CrudItem;

  newItem: CrudItem = {
    name: '',
    fatherName: '',
    age: null,
    dateOfBirth: '',
    designation: '',
    email: '',
    username: ''
  };

  emailError = false;
  isLoading = false;
  errorMessage = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private service: CrudloginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = localStorage.getItem('username') || '';
    this.role = localStorage.getItem('role') || 'user';
    
    // 🔥 NORMALIZE ROLE (handle case sensitivity)
    this.role = this.role.toUpperCase();
    
    if (!this.username) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.service.getAll(this.username).subscribe({
      next: (res: CrudItem[]) => {
        this.dataSource.data = res;
        this.dataSource.paginator = this.paginator;
        this.isLoading = false;
      },
      error: (err: Error) => {
        console.error('Load failed:', err);
        this.errorMessage = err.message || 'Failed to load data';
        this.isLoading = false;

        if (err.message.includes('User not found')) {
          setTimeout(() => {
            localStorage.removeItem('username');
            localStorage.removeItem('role');
            this.router.navigate(['/login']);
          }, 2000);
        }
      }
    });
  }

  applyFilter(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }

  validateEmail(email: string): boolean {
    const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.emailError = !pattern.test(email);
    return !this.emailError;
  }

  addItem(): void {
    if (!this.newItem.name?.trim()) { alert('Name required'); return; }
    if (!this.newItem.email?.trim()) { alert('Email required'); return; }
    if (!this.validateEmail(this.newItem.email)) { alert('Invalid email'); return; }

    this.isLoading = true;
    this.newItem.username = this.username;

    this.service.add(this.newItem).subscribe({
      next: () => {
        this.newItem = { name:'', fatherName:'', age:null, dateOfBirth:'', designation:'', email:'', username:'' };
        this.loadData();
      },
      error: (err: Error) => {
        console.error('Add failed:', err);
        this.errorMessage = err.message;
        this.isLoading = false;
      }
    });
  }

  openEdit(item: CrudItem): void {
    this.selected = { ...item };
    this.showModal = true;
    this.emailError = false;
    this.errorMessage = '';
  }

  saveEdit(): void {
    if (!this.selected.id) return;
    if (!this.selected.name?.trim() || !this.selected.email?.trim() || !this.validateEmail(this.selected.email)) {
      alert('Name and valid Email required'); return;
    }

    this.isLoading = true;
    this.service.update(this.selected.id, this.selected).subscribe({
      next: () => { this.showModal = false; this.loadData(); },
      error: (err: Error) => { 
        console.error('Update error:', err); 
        this.isLoading = false; 
        alert(err.message || 'Update failed'); 
      }
    });
  }

  delete(item: CrudItem): void {
    if (!item.id || !confirm(`Delete "${item.name}"?`)) return;
    this.isLoading = true;
    this.service.delete(item.id).subscribe({
      next: () => this.loadData(),
      error: (err: Error) => { 
        console.error('Delete error:', err); 
        this.isLoading = false; 
        alert(err.message || 'Delete failed'); 
      }
    });
  }

  // 🔥 FIX: Check ROLE instead of username
  canEdit(item: CrudItem): boolean {
    return this.role === 'ADMIN' || item.username === this.username;
  }

  closeModal(): void {
    this.showModal = false; 
    this.emailError = false; 
    this.errorMessage = '';
  }

  logout(): void {
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }
}