package com.ps.project.repository;

import com.ps.project.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findFirstById(Long id);
    User findFirstByName(String name);
    User findFirstByEmail(String email);
    User findFirstByUsername(String username);
    User findFirstByUsernameAndPassword(String username, String password);
}
