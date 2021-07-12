package com.ps.project.controller;

import com.ps.project.vo.UserDetailsDTO;
import com.ps.project.vo.request.LoginRequest;
import com.ps.project.vo.request.SignupRequest;
import com.ps.project.vo.response.JwtResponse;
import com.ps.project.vo.response.MessageResponse;
import com.ps.project.vo.response.ProfileDataResponse;
import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.User;
import com.ps.project.service.SecurityService;
import com.ps.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    private Set<String> loggedUser = new HashSet<>();


    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.isUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.isEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = new User(signupRequest);

        userService.addUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            JwtResponse response = securityService.login(userDetailsDTO);
            loggedUser.add(response.getUsername());
            return ResponseEntity.ok(response);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Wrong credentials!"));
        }
    }

    @PostMapping("/setLogout")
    public ResponseEntity<?> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            loggedUser.remove(auth.getName());
        }
        return ResponseEntity.ok(new MessageResponse("Logged out from server"));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserById(@PathVariable("username") String username){
        try {
            User user = userService.findByUsername(username);
            ProfileDataResponse response = new ProfileDataResponse(user);
            return ResponseEntity.ok(response);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

    @GetMapping("/admin/loggedInUsers")
    public ResponseEntity<?> getLoggedInUsers(){
        return ResponseEntity.ok(loggedUser.size());
    }
}
