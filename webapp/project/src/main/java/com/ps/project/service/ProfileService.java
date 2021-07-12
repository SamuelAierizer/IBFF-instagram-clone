package com.ps.project.service;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Profile;
import com.ps.project.model.User;
import org.springframework.stereotype.Component;

@Component
public interface ProfileService {
    void addProfile(Profile profile);
    void deleteUser(Profile profile);
    void updateProfile(Profile dto);
    void followUpdateProfile(Profile profile);
    Profile findByUser(User user) throws ApiExceptionResponse;
    Profile findById(Long Id) throws ApiExceptionResponse;
}