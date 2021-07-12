package com.ps.project.utils.listener;

import com.ps.project.model.Admin;

import javax.persistence.PostPersist;

public class AdminEventListener {
    @PostPersist
    public void notifyCreation(Admin admin) {
        //System.out.println("Created new admin " + admin.getName());
    }

}
