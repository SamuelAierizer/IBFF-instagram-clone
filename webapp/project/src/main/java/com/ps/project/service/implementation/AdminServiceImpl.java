package com.ps.project.service.implementation;

import com.ps.project.repository.AdminRepository;
import com.ps.project.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
}
