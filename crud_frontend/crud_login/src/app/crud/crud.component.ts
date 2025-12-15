import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crud',
  standalone: false,
  templateUrl: './crud.component.html',
  styleUrls: ['./crud.component.css']
})
export class CrudComponent implements OnInit {

  username: string = '';
  items: any[] = [];

  // Modal control
  showModal = false;
  selected: any = {};

  // Form for new item
  newItem: any = {
    name: '',
    fatherName: '',
    age: null,
    dateOfBirth: '',
    designation: ''
  };

  apiUrl = 'http://localhost:8080/api/crud';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    // Frontend session check
    this.username = localStorage.getItem('username') || '';

    if (!this.username) {
      alert('Please login first');
      this.router.navigate(['/']); // Redirect to login page
      return;
    }

    // Load CRUD data
    this.loadData();

    // Back-button prevention
    history.pushState(null, '', location.href);
    window.onpopstate = () => {
      history.go(1);
    };
  }

  loadData() {
    this.http.get<any[]>(`${this.apiUrl}?username=${this.username}`)
      .subscribe(
        res => this.items = res,
        err => {
          if(err.status === 401) {
            alert('Session expired. Please login again.');
            localStorage.removeItem('username');
            this.router.navigate(['/']);
          }
        }
      );
  }

  addItem() {
    if(!this.newItem.name.trim()) { alert('Enter Name'); return; }
    this.newItem.username = this.username;

    this.http.post(this.apiUrl, this.newItem)
      .subscribe(
        () => {
          this.newItem = { name: '', fatherName: '', age: null, dateOfBirth: '', designation: '' };
          this.loadData();
        },
        err => {
          if(err.status === 401) {
            alert('Session expired. Please login again.');
            localStorage.removeItem('username');
            this.router.navigate(['/']);
          }
        }
      );
  }

  openEdit(item: any) {
    this.selected = { ...item };
    this.showModal = true;
  }

  saveEdit() {
    this.selected.username = this.username;

    this.http.put(`${this.apiUrl}/${this.selected.id}`, this.selected)
      .subscribe(
        () => {
          this.showModal = false;
          this.loadData();
        },
        err => {
          if(err.status === 401) {
            alert('Session expired. Please login again.');
            localStorage.removeItem('username');
            this.router.navigate(['/']);
          }
        }
      );
  }

  delete(item: any) {
    if(!confirm('Are you sure?')) return;

    this.http.delete(`${this.apiUrl}/${item.id}?username=${this.username}`)
      .subscribe(
        () => this.loadData(),
        err => {
          if(err.status === 401) {
            alert('Session expired. Please login again.');
            localStorage.removeItem('username');
            this.router.navigate(['/']);
          }
        }
      );
  }

  canEdit(item: any): boolean {
    const currentUser = this.username?.trim().toLowerCase();
    const itemUser = item.user?.name?.trim().toLowerCase();
    return currentUser === 'admin' || currentUser === itemUser;
  }

  logout() {
    if(confirm('Are you sure you want to logout?')) {
      localStorage.removeItem('username');
      this.router.navigate(['/']); // Redirect to login
    }
  }

}
