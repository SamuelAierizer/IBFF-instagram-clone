package com.ps.project;

import com.ps.project.vo.UserDetailsDTO;
import com.ps.project.model.*;

import com.ps.project.repository.ProfileRepository;
import com.ps.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.ArrayList;

@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CommandLineRunner init(UserRepository userRepository, ProfileRepository profileRepository){
        return args -> {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


            UserDetailsDTO userDTO = new UserDetailsDTO("root", "Root@2357");

            userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

            Admin root = new Admin("aierizersamuel@yahoo.com", userDTO, "sudo@0000");
            root.setRole("ROLE_ADMIN");
            root.setActive(true);

            User user1 = new User(null, "Test User", "test@test.com", "testUsername", null, true,"$2a$10$E7t2m.yqcZqA5zgjUjRDa.Uq17JPmskkEHEtcJcKMI9HXxJOZQD3S", null, null, null, null, null);
            User user2 = new User(null, "Test User 2", "test2@test.com", "testUserName2", null, true,"$2a$10$yQ9giSU0KRpQ78JHNJB36uM/.JQaCqZN9rY6FBkHacdfq/uqgAbhu", null, null, null, null, null);
            User user3 = new User(null, "Test User 3", "test3@test.com", "testUserName3", null, true,"$2a$10$E7t2m.yqcZqA5zgjUjRDa.Uq17JPmskkEHEtcJcKMI9HXxJOZQD3S", null,  null, null, null, null);
            user1.setRole("ROLE_USER");
            user2.setRole("ROLE_USER");
            user3.setRole("ROLE_USER");

            Profile profileRoot = new Profile(root);
            Profile profile1 = new Profile(user1);
            Profile profile2 = new Profile(user2);
            Profile profile3 = new Profile(user3);

            root.setProfile(profileRoot);
            user1.setProfile(profile1);
            user2.setProfile(profile2);
            user3.setProfile(profile3);

            ArrayList<User> users = new ArrayList<>();
            users.add(root);
            users.add(user1);
            users.add(user2);
            users.add(user3);

            ArrayList<Profile> profiles = new ArrayList<>();
            profiles.add(profileRoot);
            profiles.add(profile1);
            profiles.add(profile2);
            profiles.add(profile3);

            userRepository.saveAll(users);
            profileRepository.saveAll(profiles);
        };
    }
}
