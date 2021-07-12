package com.ps.project.repository;

import com.ps.project.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post,Long> {
    Post findFirstById(Long Id);
}
