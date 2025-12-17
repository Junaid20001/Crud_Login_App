package com.logincrud.login_crud.service;

import com.logincrud.login_crud.dto.CrudRequest;
import com.logincrud.login_crud.model.Crud;
import com.logincrud.login_crud.model.Login;
import com.logincrud.login_crud.repository.CrudRepository;
import com.logincrud.login_crud.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudService {

    @Autowired
    private CrudRepository crudRepo;

    @Autowired
    private LoginRepository loginRepo;

    private boolean isAdmin(Login u) {
        return u != null && "admin".equalsIgnoreCase(u.getRole());
    }

    public List<Crud> getAll(String username) {
        Login user = loginRepo.findByName(username);
        if (user == null) throw new RuntimeException("User not found");

        return isAdmin(user) ? crudRepo.findAll()
                : crudRepo.findByUser_Name(username);
    }

    public Crud create(CrudRequest r) {
        Login user = loginRepo.findByName(r.getUsername());
        if (user == null) throw new RuntimeException("User not found");

        Crud c = new Crud();
        c.setName(r.getName());
        c.setFatherName(r.getFatherName());
        c.setAge(r.getAge());
        c.setDateOfBirth(r.getDateOfBirth());
        c.setDesignation(r.getDesignation());
        c.setEmail(r.getEmail());
        c.setUser(user);

        return crudRepo.save(c);
    }

    public boolean canEdit(Login user, Crud c) {
        if(user == null) return false;
        return isAdmin(user) || c.getUser().getId() == user.getId();
    }
}
