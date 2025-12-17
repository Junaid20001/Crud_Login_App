package com.logincrud.login_crud.repository;

import com.logincrud.login_crud.model.Crud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrudRepository extends JpaRepository<Crud, Integer> {
    List<Crud> findByUser_Name(String name);
}
