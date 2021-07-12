package com.ps.project.service;

import com.ps.project.vo.UserDetailsDTO;
import com.ps.project.vo.response.JwtResponse;
import com.ps.project.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

@Component
public interface SecurityService {
    JwtResponse login(UserDetailsDTO requestUser) throws ApiExceptionResponse;
}
