package com.logincrud.login_crud.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.logincrud.login_crud.model.Login;
import com.logincrud.login_crud.repository.LoginRepository;

@Service
public class LoginService {
    @Autowired
    private LoginRepository repo;

    public Login authenticate(String name, String password) {
        return repo.findByNameAndPassword(name, password);
    }

}
