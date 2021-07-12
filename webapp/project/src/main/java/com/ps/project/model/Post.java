package com.ps.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ps.project.utils.listener.PostEventListener;
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
@EntityListeners(PostEventListener.class)
public class Post implements Comparable<Post>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String caption;

    @Lob
    private String imageData;

    @ManyToOne
    @JsonBackReference
    private User user;

    @ManyToMany
    @JsonBackReference
    private List<User> likes;


    @Override
    public int compareTo(Post o) {
        return (int) (this.id - o.getId());
    }
}
