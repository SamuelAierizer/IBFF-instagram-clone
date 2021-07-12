package com.ps.project.utils.listener;

import com.ps.project.model.User;

import javax.persistence.PostPersist;

public class UserEventListener {

    @PostPersist
    public void notifyCreation(User user) {
        //System.out.println("Created user " + user.getName());
    }
}

