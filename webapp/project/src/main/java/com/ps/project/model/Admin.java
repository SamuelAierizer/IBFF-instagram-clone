package com.ps.project.model;

import com.ps.project.vo.UserDetailsDTO;
import com.ps.project.utils.listener.AdminEventListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(AdminEventListener.class)
public class Admin extends User{
    private String adminKey;

    public Admin (String email, UserDetailsDTO userDTO, String adminKey) {
        this.adminKey = adminKey;

        super.setName("User " + userDTO.getUsername().toUpperCase());
        super.setEmail(email);
        super.setUsername(userDTO.getUsername());
        super.setPassword(userDTO.getPassword());
    }
}
