package com.logincrud.login_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.logincrud.login_crud.dto.LoginRequest;
import com.logincrud.login_crud.model.Login;
import com.logincrud.login_crud.service.LoginService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private LoginService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Login user = service.authenticate(req.getName(), req.getPassword());

        if (user != null) {
            // 🔥 Return username and role as JSON
            Map<String, String> response = new HashMap<>();
            response.put("username", user.getName());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid");
    }
}