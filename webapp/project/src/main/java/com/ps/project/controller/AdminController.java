package com.ps.project.controller;

import com.ps.project.vo.request.UserEditRequest;
import com.ps.project.vo.response.MessageResponse;
import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.User;
import com.ps.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping({"", "/"})
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("/edit/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserEditRequest user) {
        try {
            User newUser = userService.findById(id);
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setUsername(user.getUsername());
            userService.updateUser(newUser);
            return ResponseEntity.ok(new MessageResponse("User updated successfully"));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

    @DeleteMapping("/delete/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            User user = userService.findById(id);
            userService.deleteUser(user);
            return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

}
