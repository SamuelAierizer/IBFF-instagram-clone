package com.ps.project.service.implementation;

import com.ps.project.model.User;
import com.ps.project.service.UserService;
import com.ps.project.vo.UserDetailsDTO;
import com.ps.project.vo.response.JwtResponse;
import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.service.SecurityService;
import com.ps.project.web.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;


    @Override
    public JwtResponse login(UserDetailsDTO requestUser) throws ApiExceptionResponse {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUsername(userDetails.getUsername());

            return new JwtResponse(jwt,
                    user.getId(),
                    user.getUsername(),
                    user.getRole()
            );
        } catch (ApiExceptionResponse e) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Bad credentials"))
                    .message("User not found").status(HttpStatus.NOT_FOUND).build();
        }
    }
}