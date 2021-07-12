package com.ps.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ps.project.utils.listener.ProfileEventListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(ProfileEventListener.class)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String url;

    private String image;

    @OneToOne
    @JsonBackReference
    private User user;

    @ManyToMany
    @JsonBackReference
    private List<User> followers;

    public Profile (User user) {
        title = "Set Your Title";
        description = "Set Your Description";
        url = "Set Your URL";
        image = "default-starter.png";
        this.user = user;
    }

    public String getImagePath() {
        if (image.isEmpty()) {
            return "images/default.png";
        }
        return "images/" + this.image;
    }

    public Integer getFollowersCount() {
        if (followers != null){
            if (!followers.isEmpty()) {
                return followers.size();
            }
        }
        return 0;
    }
}
