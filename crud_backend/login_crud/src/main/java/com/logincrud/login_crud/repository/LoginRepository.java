package com.logincrud.login_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.logincrud.login_crud.model.Login;

public interface LoginRepository extends JpaRepository<Login, Integer> {

    Login findByNameAndPassword(String name, String password);
    Login findByName(String name);

}
