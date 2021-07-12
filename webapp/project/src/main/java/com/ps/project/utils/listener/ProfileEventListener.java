package com.ps.project.utils.listener;

import com.ps.project.model.Profile;

import javax.persistence.PostPersist;

public class ProfileEventListener {

    @PostPersist
    public void notifyCreation(Profile profile){
        //System.out.println("Created profile of user: " + profile.getUser().getId() + " with title: " + profile.getTitle());

//        notificationService.sendNotify(..);
    }
}
