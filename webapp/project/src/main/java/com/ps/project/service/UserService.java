package com.ps.project.service;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    void followUpdateUser(User user);
    void updateUser(User dto);
    void updatePosts(User user);
    void addUser(User user);
    void deleteUser(User user);
    List<User> findAll();
    User findById(Long id) throws ApiExceptionResponse;
    User findByName(String name) throws ApiExceptionResponse;
    User findByUsername(String username) throws ApiExceptionResponse;
    boolean isUsername(String username);
    boolean isEmail(String email);
}
