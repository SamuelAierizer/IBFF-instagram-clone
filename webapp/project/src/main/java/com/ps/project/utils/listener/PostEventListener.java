package com.ps.project.utils.listener;

import com.ps.project.model.Post;

import javax.persistence.PostPersist;

public class PostEventListener {

    @PostPersist
    public void notifyCreation(Post post){
        //System.out.println("Created post " + post.getId() + " with caption: " + post.getCaption() +
        //        "\n\tof user: " + post.getUser().getId());
    }
}
