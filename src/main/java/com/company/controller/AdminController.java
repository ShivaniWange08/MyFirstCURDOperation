package com.company.controller;

import com.company.entity.Admin;
import com.company.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addAdmin")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin){
        System.err.println(admin);
        adminService.saveAdmin(admin);

        String msg = "Heyy..." +admin.getFName() +", Your profile has been successfully added, Thank You!!";
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/getAllAdmin")
    public ResponseEntity<List<Admin>> getAllDat(){
        List<Admin> adminList = adminService.getAllAdmin();
        return new ResponseEntity<>(adminList, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable("id") int id){
       Admin admin = adminService.getAdminById(id);
       return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ("id") int id){
        String delete = adminService.deleteAdminByIde(id);
        String msg = "Heyy..., Your profile is Deleted, Thank You!!";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
