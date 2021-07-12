package com.ps.project.controller;

import com.ps.project.vo.request.LikeRequest;
import com.ps.project.vo.request.PostCreateRequest;
import com.ps.project.vo.response.MessageResponse;
import com.ps.project.vo.response.PostResponse;
import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Post;
import com.ps.project.model.User;
import com.ps.project.service.PostService;
import com.ps.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile/p")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class PostController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/{id}/create")
    @ResponseBody
    public ResponseEntity<?> createPost(@PathVariable("id") Long id, @RequestBody PostCreateRequest postRequest) {
        try {
            User user = userService.findById(id);
            List<Post> postList = user.getPosts();

            Post post = new Post();
            post.setCaption(postRequest.getCaption());
            post.setUser(user);

            postService.savePost(post, postRequest.getFile());

            postList.add(post);
            user.setPosts(postList);
            userService.updatePosts(user);

        } catch (Exception  e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error while trying to create post!"));
        }
        return ResponseEntity.ok(new MessageResponse("Post added successfully!"));
    }

    @GetMapping("/{username}/feed")
    public ResponseEntity<?> getFeed(@PathVariable("username") String username) {
        try {
            List<PostResponse> posts = postService.findUserFeed(username)
                    .stream().sorted(Comparator.reverseOrder()).map(post -> new PostResponse(
                            post.getId(),
                            post.getCaption(),
                            post.getUser().getName(),
                            post.getUser().getUsername(),
                            post.getLikes().size(),
                            post.getImageData()
                            //new String(post.getImageData(), StandardCharsets.UTF_8)
                    )).collect(Collectors.toList());

            return ResponseEntity.ok(posts);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

    @GetMapping("/{username}/all")
    public ResponseEntity<?> getAllForUser(@PathVariable("username") String username) {
        try {
            User user = userService.findByUsername(username);
            List<PostResponse> posts = user.getPosts().stream().sorted(Comparator.reverseOrder())
                    .map(post -> new PostResponse(
                    post.getId(),
                    post.getCaption(),
                    user.getName(),
                    user.getUsername(),
                    post.getLikes().size(),
                    post.getImageData()
                    //new String(post.getImageData(), StandardCharsets.UTF_8)
            )).collect(Collectors.toList());

            return ResponseEntity.ok(posts);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

    @GetMapping("/files/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable("post_id") Long postId) {
        try {
            Post post = postService.findById(postId);

            PostResponse postResponse = new PostResponse(
                    post.getId(),
                    post.getCaption(),
                    post.getUser().getName(),
                    post.getUser().getUsername(),
                    post.getLikes().size(),
                    post.getImageData()
                    //new String(post.getImageData(), StandardCharsets.UTF_8)
            );

            return ResponseEntity.ok(postResponse);
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find post"));
        }
    }

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody LikeRequest likeRequest) {
        try {
            String username = likeRequest.getUsername();
            Long postId = likeRequest.getPostId();

            User user = userService.findByUsername(username);
            Post post = postService.findById(postId);

            List<User> liked = post.getLikes();
            liked.add(user);

            post.setLikes(liked);

            postService.updateLike(post);

            //Websocket notification for user, that someone liked their post
            MessageResponse notification = new MessageResponse(username + " has liked your post!");
            template.convertAndSendToUser(post.getUser().getUsername(),"/notify", notification);

            return ResponseEntity.ok(liked.size());
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user or post"));
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<?> dislike(@RequestBody LikeRequest dislikeRequest) {
        try {
            String username = dislikeRequest.getUsername();
            Long postId = dislikeRequest.getPostId();

            User user = userService.findByUsername(username);
            Post post = postService.findById(postId);

            List<User> liked = post.getLikes();

            if(liked.contains(user)){
                liked.remove(user);

                post.setLikes(liked);

                postService.updateLike(post);
                return ResponseEntity.ok(liked.size());
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("This user didn't like this post before"));
            }
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user or post"));
        }
    }

    @GetMapping("{username}/liked/{post_id}")
    public ResponseEntity<?> hasLiked(@PathVariable("username") String username, @PathVariable("post_id") Long post_id){
        try {
            User user = userService.findByUsername(username);
            Post post = postService.findById(post_id);

            return ResponseEntity.ok(post.getLikes().contains(user));
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user or post"));
        }
    }

    @GetMapping("{post_id}/likeCount")
    public ResponseEntity<?> getLikeCount(@PathVariable("post_id") Long post_id) {
        try {
            return ResponseEntity.ok(postService.findById(post_id).getLikes().size());
        } catch (ApiExceptionResponse apiExceptionResponse) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find post"));
        }
    }

    @PostMapping("/share")
    public ResponseEntity<?> share(@RequestBody LikeRequest request) {
        try {
            String username = request.getUsername();
            Long postId = request.getPostId();

            User user = userService.findByUsername(username);
            Post post = postService.findById(postId);

            Post newPost = new Post();
            newPost.setCaption(post.getCaption());
            newPost.setUser(user);

            postService.savePost(newPost, post.getImageData());

            List<Post> postList = user.getPosts();
            postList.add(newPost);

            user.setPosts(postList);

            userService.updatePosts(user);

            //Websocket notification for user, that someone liked their post
            MessageResponse notification = new MessageResponse(username + " has shared your post!");
            template.convertAndSendToUser(post.getUser().getUsername(),"/notify", notification);

            return ResponseEntity.ok(new MessageResponse("Shared successfully"));
        } catch (Exception apiExceptionResponse) {
            System.out.println(apiExceptionResponse.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user or post"));
        }
    }

}
