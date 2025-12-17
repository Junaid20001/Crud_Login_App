package com.logincrud.login_crud.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "crud_items")
public class Crud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "father_name")
    private String fatherName;

    private int age;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String designation;
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Login user;

    @Transient
    private String username;

    @PostLoad
    private void loadUsername() {
        if (user != null) {
            this.username = user.getName();
        }
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public int getAge() { return age; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getDesignation() { return designation; }
    public String getEmail() { return email; }
    public Login getUser() { return user; }
    public String getUsername() { return username; }

    public void setName(String name) { this.name = name; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public void setAge(int age) { this.age = age; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setEmail(String email) { this.email = email; }
    public void setUser(Login user) { this.user = user; }
}
