package com.ps.project.service.implementation;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Comment;
import com.ps.project.model.Post;
import com.ps.project.model.User;
import com.ps.project.repository.CommentRepository;
import com.ps.project.repository.PostRepository;
import com.ps.project.repository.UserRepository;
import com.ps.project.service.CommentService;
import com.ps.project.utils.exporter.FileExporter;
import com.ps.project.utils.exporter.XMLFileExporter;
import com.ps.project.vo.request.CommentRequest;
import com.ps.project.vo.response.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(CommentRequest commentRequest) throws ApiExceptionResponse {
        Post post = postRepository.findFirstById(commentRequest.getPostId());
        User user = userRepository.findFirstByUsername(commentRequest.getUsername());

        if (post == null || user == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Could not find user or post"))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        Comment comment = new Comment();

        comment.setPost(post);
        comment.setUser(user);
        comment.setText(commentRequest.getText());

        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponse> findAll(Long postId) throws ApiExceptionResponse {
        Post post = postRepository.findFirstById(postId);

        if (post == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Could not find post"))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        return commentRepository.findAllByPost(post).stream().parallel()
                .sorted(Comparator.reverseOrder())
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getUser().getUsername(),
                        comment.getText()
                )).collect(Collectors.toList());
    }

    @Override
    public String exportComments(Long postId) {
        Post post = postRepository.findFirstById(postId);
        List<Comment> comments = commentRepository.findAllByPost(post);

        FileExporter fileExporter = new XMLFileExporter();

        return fileExporter.exportData(comments);
    }
}
