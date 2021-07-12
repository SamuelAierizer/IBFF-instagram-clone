package com.ps.project.controller;

import com.ps.project.vo.request.FollowRequest;
import com.ps.project.vo.request.ProfileEditRequest;
import com.ps.project.vo.response.FollowNumbersDataResponse;
import com.ps.project.vo.response.MessageResponse;
import com.ps.project.exceptions.ApiExceptionResponse;
import com.ps.project.model.Profile;
import com.ps.project.model.User;
import com.ps.project.service.ProfileService;
import com.ps.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @PostMapping("/follows")
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest) {
        try {
            String username = followRequest.getUsername();
            String target = followRequest.getTarget();

            User follower = userService.findByUsername(username);
            User followed;

            if (username.contentEquals(target)) {
                followed = follower;
            } else {
                followed  = userService.findByUsername(target);
            }

            List<Profile> followingProfiles = follower.getFollowing();
            Profile followedProfile = followed.getProfile();
            List<User> followingUsers = followedProfile.getFollowers();

            followingProfiles.add(followedProfile);
            follower.setFollowing(followingProfiles);

            followingUsers.add(follower);
            followedProfile.setFollowers(followingUsers);

            userService.followUpdateUser(follower);
            profileService.followUpdateProfile(followedProfile);

            return ResponseEntity.ok(new MessageResponse(username + " now follows " + target));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed follow request"));
        }
    }

    @PostMapping("/unfollows")
    public ResponseEntity<?> unfollowUser(@RequestBody FollowRequest unfollowRequest) {
        try {
            String username = unfollowRequest.getUsername();
            String target = unfollowRequest.getTarget();
            User follower;
            follower = userService.findByUsername(username);
            User followed;

            if (username.contentEquals(target)) {
                followed = follower;
            } else {
                followed  = userService.findByUsername(target);
            }

            List<Profile> followingProfiles = follower.getFollowing();
            Profile followedProfile = followed.getProfile();
            List<User> followingUsers = followedProfile.getFollowers();

            followingProfiles.remove(followedProfile);
            follower.setFollowing(followingProfiles);

            followingUsers.remove(follower);
            followedProfile.setFollowers(followingUsers);

            userService.followUpdateUser(follower);
            profileService.followUpdateProfile(followedProfile);

            return ResponseEntity.ok(new MessageResponse(username + " doesn't follows " + target + " anymore"));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed follow request"));
        }
    }

    @GetMapping("/{username}/numbers")
    public ResponseEntity<?> getNumbers(@PathVariable("username") String username) {
        try {
            User user = userService.findByUsername(username);
            FollowNumbersDataResponse response = new FollowNumbersDataResponse(user);
            return ResponseEntity.ok(response);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }

    @GetMapping("/{username}/check/{target}")
    public ResponseEntity<?> checkFollowing(@PathVariable("username") String username, @PathVariable("target") String target) {
        try {
            User follower;
            User followed;
            follower = userService.findByUsername(username);

            if (username.contentEquals(target)) {
                followed = follower;
            } else {
                followed  = userService.findByUsername(target);
            }

            List<Profile> followingProfiles = follower.getFollowing();
            Profile followedProfile = followed.getProfile();

            boolean isFollowing;

            if (followingProfiles.isEmpty()) {
                isFollowing = false;
            } else {
                isFollowing = followingProfiles.contains(followedProfile);
            }

            return ResponseEntity.ok(new MessageResponse("" + isFollowing));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed check follow request"));
        }
    }



    @PutMapping("/{username}/update")
    @ResponseBody
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody ProfileEditRequest profile){
        try {
            Profile newProfile = userService.findByUsername(username).getProfile();
            newProfile.setTitle(profile.getTitle());
            newProfile.setDescription(profile.getDescription());
            newProfile.setUrl(profile.getUrl());
            profileService.updateProfile(newProfile);
            return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Could not find user"));
        }
    }
}
