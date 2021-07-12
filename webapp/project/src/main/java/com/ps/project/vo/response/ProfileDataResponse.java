package com.ps.project.vo.response;

import com.ps.project.model.Profile;
import com.ps.project.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDataResponse {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String image;

    public ProfileDataResponse(User user) {
        Profile profile = user.getProfile();

        this.id = profile.getId();
        this.title = profile.getTitle();
        this.description = profile.getDescription();
        this.url = profile.getUrl();
        this.image = profile.getImage();
    }
}
