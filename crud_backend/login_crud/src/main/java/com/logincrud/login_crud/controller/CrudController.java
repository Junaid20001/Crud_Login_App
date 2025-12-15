package com.logincrud.login_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.logincrud.login_crud.dto.CrudRequest;
import com.logincrud.login_crud.model.Crud;
import com.logincrud.login_crud.service.CrudService;
import java.util.List;

@RestController
@RequestMapping("/api/crud")
@CrossOrigin("*")
public class CrudController {

    @Autowired
    private CrudService crudService;

    @GetMapping
    public List<Crud> getAll(@RequestParam String username) {
        return crudService.getAll(username);
    }

    @PostMapping
    public Crud create(@RequestBody CrudRequest request) {
        return crudService.create(request);
    }

    @PutMapping("/{id}")
    public Crud update(@PathVariable int id, @RequestBody CrudRequest request) {
        return crudService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestParam String username) {
        crudService.delete(id, username);
    }
}
