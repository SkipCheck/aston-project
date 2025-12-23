package com.aston.dao;

import com.aston.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    User update(User user);
    List<User> findAll();
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    void delete(Long id);
    boolean existsById(Long id);
    boolean existsByEmail(String email);
}