package com.ps.project.service.implementation;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Profile;
import com.ps.project.model.User;
import com.ps.project.repository.ProfileRepository;
import com.ps.project.repository.UserRepository;
import com.ps.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void followUpdateUser(User user) {
        User persistedUser = userRepository.findById(user.getId()).get();
        persistedUser.setFollowing(user.getFollowing());
        userRepository.save(persistedUser);
    }

    @Override
    public void updateUser(User dto) {
        User persistedUser = userRepository.findById(dto.getId()).get();
        persistedUser.setName(dto.getName());
        persistedUser.setEmail(dto.getEmail());
        persistedUser.setUsername(dto.getUsername());
        userRepository.save(persistedUser);
    }

    @Override
    public void updatePosts(User user) {
        User persistedUser = userRepository.findById(user.getId()).get();
        persistedUser.setPosts(user.getPosts());
        userRepository.save(persistedUser);
    }

    // Security for password
    @Override
    public void addUser(User user) {
        Profile profile = new Profile(user);

        user.setRole("ROLE_USER");
        if (user.getClass().toString().toUpperCase().contains("ADMIN")) {
             user.setRole("ROLE_ADMIN");
        }

        user.setProfile(profile);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        profileRepository.save(profile);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User findById(Long id) throws ApiExceptionResponse {
        User user = userRepository.findFirstById(id);

        if (user == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Nu user with id: " + id))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        return user;
    }

    @Override
    public User findByName(String name) throws ApiExceptionResponse {
        User user = userRepository.findFirstByName(name);

        if (user == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("No user with name: " + name))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        return user;
    }

    @Override
    public User findByUsername(String username) throws ApiExceptionResponse {
        User user = userRepository.findFirstByUsername(username);

        if (user == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("No user with username: " + username))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        return user;
    }

    @Override
    public boolean isUsername(String username){
        User user = userRepository.findFirstByUsername(username);

        return user != null;
    }

    @Override
    public boolean isEmail(String email){
        User user = userRepository.findFirstByEmail(email);

        return user != null;
    }
}
