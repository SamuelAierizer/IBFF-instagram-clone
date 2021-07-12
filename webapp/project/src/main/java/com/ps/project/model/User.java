package com.ps.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ps.project.vo.request.SignupRequest;
import com.ps.project.utils.listener.UserEventListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(UserEventListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String username;

    private LocalDate emailVerifiedAt;

    private boolean active;

    private String password;

    private String role;

    @Transient
    private String passwordConfirm;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Post> posts;

    @OneToOne(cascade = {CascadeType.ALL})
    @JsonManagedReference
    private Profile profile;

    @ManyToMany
    @JsonManagedReference
    private List<Profile> following;

    public User (SignupRequest signupRequest) {
        this.name = signupRequest.getName();
        this.email = signupRequest.getEmail();
        this.username = signupRequest.getUsername();
        this.password = signupRequest.getPassword();
    }

    public Integer getPostCount() {
        if (posts != null){
            if (!posts.isEmpty()) {
                return posts.size();
            }
        }
        return 0;
    }

    public Integer getFollowingCount() {
        if (following != null){
            if (!following.isEmpty()) {
                return following.size();
            }
        }
        return 0;
    }

}