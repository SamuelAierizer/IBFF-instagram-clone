package com.ps.project.repository;

import com.ps.project.model.Comment;
import com.ps.project.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
