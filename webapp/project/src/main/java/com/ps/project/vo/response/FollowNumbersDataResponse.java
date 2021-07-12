package com.ps.project.vo.response;

import com.ps.project.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FollowNumbersDataResponse {
    private Integer postCount;
    private Integer followersCount;
    private Integer followingCount;

    public FollowNumbersDataResponse(User user) {
        postCount = user.getPostCount();
        followersCount = user.getProfile().getFollowersCount();
        followingCount = user.getFollowingCount();
    }
}
