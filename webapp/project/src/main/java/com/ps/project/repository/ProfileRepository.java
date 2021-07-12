package com.ps.project.repository;

import com.ps.project.model.Profile;
import com.ps.project.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<Profile,Long> {
    Profile findFirstByUser(User user);
    Profile findFirstById(Long id);
}
