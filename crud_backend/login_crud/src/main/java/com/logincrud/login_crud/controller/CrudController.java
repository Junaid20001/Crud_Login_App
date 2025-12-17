package com.logincrud.login_crud.controller;

import com.logincrud.login_crud.dto.CrudRequest;
import com.logincrud.login_crud.model.Crud;
import com.logincrud.login_crud.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crud")
@CrossOrigin("*")
public class CrudController {

    @Autowired
    private CrudService service;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username is required"));
        }
        List<Crud> list = service.getAll(username);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CrudRequest req) {
        if (req.getName() == null || req.getName().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Name is required"));

        if (req.getEmail() == null || req.getEmail().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));

        if (req.getUsername() == null || req.getUsername().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));

        Crud c = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "healthy"));
    }
}
