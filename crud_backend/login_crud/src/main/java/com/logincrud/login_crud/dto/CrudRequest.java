package com.logincrud.login_crud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class CrudRequest {

    private String name;
    private String fatherName;
    private int age;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String designation;
    private String email;
    private String username;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
