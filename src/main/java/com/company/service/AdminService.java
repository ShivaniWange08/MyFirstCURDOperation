package com.company.service;

import com.company.entity.Admin;

import java.util.List;

public interface AdminService {

    String saveAdmin(Admin admin);

    List<Admin> getAllAdmin();

    Admin getAdminById(int id);

    String deleteAdminByIde(int id);


}
