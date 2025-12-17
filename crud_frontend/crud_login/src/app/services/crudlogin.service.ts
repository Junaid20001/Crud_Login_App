import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface CrudItem {
  id?: number;
  name: string;
  fatherName: string;
  age: number | null;
  dateOfBirth: string;
  designation: string;
  email: string;
  username?: string;
}

export interface UserRole {
  username: string;
  role: string; // 'admin' or 'user'
}

@Injectable({
  providedIn: 'root'
})
export class CrudloginService {

  private springApi = 'http://localhost:8080/api/crud';
  private goApi = 'http://localhost:9090/api/crud';
  private loginApi = 'http://localhost:8080/api/login'; // Spring Boot login endpoint

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    let msg = 'Server error';
    if (error.error?.error) msg = error.error.error;
    return throwError(() => new Error(msg));
  }

  // CRUD operations
  getAll(username: string): Observable<CrudItem[]> {
    return this.http
      .get<CrudItem[]>(`${this.springApi}?username=${username}`)
      .pipe(catchError(this.handleError));
  }

  add(item: CrudItem): Observable<any> {
    return this.http
      .post(this.springApi, item)
      .pipe(catchError(this.handleError));
  }

  update(id: number, item: CrudItem): Observable<any> {
    const username = localStorage.getItem('username') || '';
    return this.http
      .put(`${this.goApi}/${id}?username=${username}`, item)
      .pipe(catchError(this.handleError));
  }

  delete(id: number): Observable<any> {
    const username = localStorage.getItem('username') || '';
    return this.http
      .delete(`${this.goApi}/${id}?username=${username}`)
      .pipe(catchError(this.handleError));
  }

  // Login with role
  login(username: string, password: string): Observable<UserRole> {
    return this.http
      .post<UserRole>(this.loginApi, { username, password })
      .pipe(catchError(this.handleError));
  }
}
