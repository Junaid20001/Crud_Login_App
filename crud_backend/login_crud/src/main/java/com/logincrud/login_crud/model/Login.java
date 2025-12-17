package com.logincrud.login_crud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "login_users")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String password;

    private String role; // 🔥 admin / user

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}
