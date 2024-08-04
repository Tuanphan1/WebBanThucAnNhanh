package com.example.foodshop.service;

import com.example.foodshop.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    User saveUser(User user);
    void deleteUser(Long id);
}
