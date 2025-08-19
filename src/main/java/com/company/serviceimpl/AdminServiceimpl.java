package com.company.serviceimpl;


import com.company.entity.Admin;
import com.company.repositories.AdminRepository;
import com.company.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdminServiceimpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public String saveAdmin(Admin admin) {
        Admin saveAdmin = adminRepository.save(admin);
        log.info("Admin saved successfully with id={}", saveAdmin.getId());
        return "Admin Saved";
    }

    @Override
    public List<Admin> getAllAdmin() {
        List<Admin> adminList = adminRepository.findAll();
        log.info("Fetched {} admins from DB", adminList.size());
        return adminList;
    }

    @Override
    public Admin getAdminById(int id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()->new NullPointerException("Admin with id not found" +id));
        log.info("Fetch admin with id={}", id);
        return admin;
    }

    @Override
    public String deleteAdminByIde(int id) {
        adminRepository.deleteById(id);
        log.info("Deleted admin with id={}", id);
        return "Admin Deleted";
    }
}
