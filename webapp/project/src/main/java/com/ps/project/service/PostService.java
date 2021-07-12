package com.ps.project.service;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Post;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Component
public interface PostService {
    void savePost(Post post, String file) throws IOException;
    void updateLike(Post post);
    Post findById(Long id) throws ApiExceptionResponse;
    List<Post> findAll();
    List<Post> findUserFeed(String username) throws ApiExceptionResponse;
}
