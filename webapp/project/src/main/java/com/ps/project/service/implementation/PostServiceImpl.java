package com.ps.project.service.implementation;

import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Post;
import com.ps.project.model.Profile;
import com.ps.project.model.User;
import com.ps.project.repository.PostRepository;
import com.ps.project.repository.UserRepository;
import com.ps.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private final Path root = Paths.get("uploads");

    @Override
    public void savePost(Post post, String file){
        post.setImageData(file);

        postRepository.save(post);
    }

    @Override
    public void updateLike(Post post) {
        Post persistedPost = postRepository.findFirstById(post.getId());
        persistedPost.setLikes(post.getLikes());
        postRepository.save(persistedPost);
    }

    @Override
    public Post findById(Long Id) throws ApiExceptionResponse {
        Post post = postRepository.findFirstById(Id);

        if (post == null){
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("No post with id: " + Id))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        post.setImageData(post.getImageData());

        return post;
    }

    @Override
    public List<Post> findAll() {
        return (List<Post>) postRepository.findAll();
    }

    @Override
    public List<Post> findUserFeed(String username) throws ApiExceptionResponse {
        User user = userRepository.findFirstByUsername(username);

        if (user == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("No user with username: " + username))
                    .message("Entity not found").status(HttpStatus.NOT_FOUND).build();
        }

        List<Post> postList = user.getPosts();

        for (Profile following : user.getFollowing()) {
            postList.addAll(following.getUser().getPosts());
        }

        return  postList;
    }
}

