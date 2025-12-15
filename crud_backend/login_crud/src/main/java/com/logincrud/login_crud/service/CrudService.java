package com.logincrud.login_crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.logincrud.login_crud.dto.CrudRequest;
import com.logincrud.login_crud.model.Crud;
import com.logincrud.login_crud.model.Login;
import com.logincrud.login_crud.repository.CrudRepository;
import com.logincrud.login_crud.repository.LoginRepository;
import java.util.List;

@Service
public class CrudService {

    @Autowired
    private CrudRepository crudRepository;

    @Autowired
    private LoginRepository loginRepository;

    private boolean isAdmin(Login user) {
        return user.getName().equalsIgnoreCase("admin");
    }

    public List<Crud> getAll(String username) {
        Login user = loginRepository.findByName(username);

        if (isAdmin(user)) {
            return crudRepository.findAll();
        }
        return crudRepository.findByUser_Name(username);
    }

    public Crud create(CrudRequest request) {
        Login user = loginRepository.findByName(request.getUsername());

        Crud crud = new Crud();
        crud.setName(request.getName());
        crud.setFatherName(request.getFatherName());
        crud.setAge(request.getAge());
        crud.setDateOfBirth(request.getDateOfBirth());
        crud.setDesignation(request.getDesignation());
        crud.setUser(user);

        return crudRepository.save(crud);
    }

    public Crud update(int id, CrudRequest request) {
        Crud crud = crudRepository.findById(id).orElseThrow();
        Login user = loginRepository.findByName(request.getUsername());

        if (!isAdmin(user) && crud.getUser().getId() != user.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        crud.setName(request.getName());
        crud.setFatherName(request.getFatherName());
        crud.setAge(request.getAge());
        crud.setDateOfBirth(request.getDateOfBirth());
        crud.setDesignation(request.getDesignation());

        return crudRepository.save(crud);
    }

    public void delete(int id, String username) {
        Crud crud = crudRepository.findById(id).orElseThrow();
        Login user = loginRepository.findByName(username);

        if (!isAdmin(user) && crud.getUser().getId() != user.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        crudRepository.delete(crud);
    }
}
