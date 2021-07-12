package com.ps.project.service;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.vo.request.CommentRequest;
import com.ps.project.vo.response.CommentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentService {
    void save(CommentRequest commentRequest) throws ApiExceptionResponse;
    List<CommentResponse> findAll(Long postId) throws ApiExceptionResponse;
    String exportComments(Long postId);
}
