package com.company.serviceimpl;


import com.company.entity.Admin;
import com.company.repositories.AdminRepository;
import com.company.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceimpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public String saveAdmin(Admin admin) {
        Admin saveAdmin = adminRepository.save(admin);
        return "Admin Saved";
    }

    @Override
    public List<Admin> getAllAdmin() {
        List<Admin> adminList = adminRepository.findAll();
        return adminList;
    }

    @Override
    public Admin getAdminById(int id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()->new NullPointerException("Admin with id not found" +id));
        return admin;
    }

    @Override
    public String deleteAdminByIde(int id) {
        adminRepository.deleteById(id);
        return "Admin Deleted";
    }
}
