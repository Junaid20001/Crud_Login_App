package com.logincrud.login_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.logincrud.login_crud.dto.LoginRequest;
import com.logincrud.login_crud.model.Login;
import com.logincrud.login_crud.service.LoginService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private LoginService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Login user = service.authenticate(request.getName(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok("Login Successful");
        } else {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}




//package com.logincrud.login_crud.controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.logincrud.login_crud.model.login;
//import com.logincrud.login_crud.service.loginService;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin("*")
//public class loginController {
//
//    @Autowired
//    private loginService service;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody login user) {
//
//        login n = service.authenticate(user.getName(), user.getPassword());
//
//        if (n != null) {
//            return ResponseEntity.ok("Login Successful");
//        } else {
//            return ResponseEntity.status(401).body("Invalid Credentials");
//        }
//    }
//}
