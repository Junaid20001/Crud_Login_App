import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

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

    this.http.post("http://localhost:8080/api/auth/login", body, { responseType: "text" })
      .subscribe(
        res => {
          
          // login.component.ts
          localStorage.setItem('username', this.name.trim()); 
          this.errorMsg = "";
          this.router.navigate(['/crud']); 
        },
        err => {
          this.errorMsg = "Invalid Username or Password";
        }
      );
  }
}
