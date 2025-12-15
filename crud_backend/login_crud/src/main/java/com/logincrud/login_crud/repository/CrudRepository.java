package com.logincrud.login_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.logincrud.login_crud.model.Crud;
import java.util.List;

public interface CrudRepository extends JpaRepository<Crud, Integer> {
    List<Crud> findByUserId(int userId);
    List<Crud> findByUser_Name(String name);
}
