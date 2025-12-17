import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface LoginResponse {
  username: string;
  role: string;
}

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  name = '';
  password = '';
  errorMsg = '';

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    const body = {
      name: this.name,
      password: this.password
    };

    
    this.http.post<LoginResponse>("http://localhost:8080/api/auth/login", body)
      .subscribe({
        next: (res) => {
          console.log('Login response:', res);
          
          // 🔥 Store username and role from the JSON response
          localStorage.setItem('username', res.username.trim());
          localStorage.setItem('role', res.role.toUpperCase());
          
          console.log('Stored username:', res.username);
          console.log('Stored role:', res.role);
          
          this.errorMsg = "";
          this.router.navigate(['/crud']);
        },
        error: (err) => {
          console.error('Login error:', err);
          this.errorMsg = "Invalid Username or Password";
        }
      });
  }
}