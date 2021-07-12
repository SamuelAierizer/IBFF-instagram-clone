package com.ps.project.controller;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Post;
import com.ps.project.service.CommentService;
import com.ps.project.service.PostService;
import com.ps.project.vo.request.CommentRequest;
import com.ps.project.vo.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody CommentRequest request) {
        try {
            commentService.save(request);

            Post post = postService.findById(request.getPostId());

            //Websocket notification for user, that someone liked their post
            MessageResponse notification = new MessageResponse(request.getUsername() + " has commented your post!");
            template.convertAndSendToUser(post.getUser().getUsername(),"/notify", notification);

            return ResponseEntity.ok(new MessageResponse("Comment submitted successfully"));
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user or post"));
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok( commentService.findAll(postId) );
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find post"));
        }
    }

    @GetMapping("/export/{postId}")
    public ResponseEntity<?> exportComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.exportComments(postId));
    }
}
