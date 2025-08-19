package com.company.controller;

import com.company.entity.Developer;
import com.company.helperDeveloper.GetExcelData;
import com.company.service.AdminService;
import com.company.service.DeveloperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.*;

@RestController
@RequestMapping("/developer")
@Slf4j
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private AdminService adminService;

    //to add developer
    @PostMapping("/addDeveloper")
    public ResponseEntity<String> addDeveloper(@Valid @RequestBody Developer developer) {
        log.info("Received request to add developer: {}", developer);
        developerService.saveDeveloper(developer);

        String msg = "Heyy..." + developer.getFName() + ", Your profile has been successfully added," + " and Your DeveloperId is : " + developer.getDeveloperId();
        log.debug("Developer {} saved successfully with ID {}", developer.getFName(), developer.getDeveloperId());
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    //to get all developer
    @GetMapping("/getAllDeveloper")
    public ResponseEntity<List<Developer>> getAllData() {
        List<Developer> developerList = developerService.getAllDeveloper();
        log.debug("List of developers found: {}", developerList.size());
        return new ResponseEntity<>(developerList, HttpStatus.OK);
    }

    //to get developer by id
    @GetMapping("/getById/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable("id") int id) {
        log.info("Get developer with ID: {}", id);
        Developer developer = developerService.getDeveloperById(id);
        return new ResponseEntity<>(developer, HttpStatus.OK);

    }

    //to delete developer by id
    @DeleteMapping("/deleteDeveloperById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") int id) {
        log.warn("Delete developer with ID: {}", id);
        String delete = developerService.deleteDeveloperById(id);
        return new ResponseEntity<>(delete, HttpStatus.OK);
    }

    //to update developer by id
    @PutMapping("/updateDeveloper/{id}")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable("id") int id, @RequestBody Developer developer) {
        log.info("Update developer with ID: {}", id);
        Developer updateDeveloper = developerService.updateDeveloper(id, developer);
        return new ResponseEntity<>(updateDeveloper, HttpStatus.OK);
    }

    //addListofDeveloper
    @PostMapping("/listOfDeveloper")
    public ResponseEntity<List<Developer>> listOfDeveloper(@Valid @RequestBody List<Developer> developerList) {
        //List<Developer> developerList1 =
        log.info("Add list of {} developers", developerList.size());
        developerService.savelistOfDeveloper(developerList);
        return new ResponseEntity<>(developerList, HttpStatus.OK);
    }

    //search developer according city, gender
    @GetMapping("/filter")
    public ResponseEntity<List<Developer>> filterDeveloper(@RequestParam(required = false) String city,
                                                           @RequestParam(required = false) String gender) {
        log.info("Filtering developers by city={} and gender={}", city, gender);
        List<Developer> sortedList = new ArrayList<>();
        if ((gender != null) && (city != null)) {
            sortedList = developerService.filterByCityAndGender(city, gender);
        } else if (city != null) {
            sortedList = developerService.filterDataByCity(city);
        } else if (gender != null) {
            sortedList = developerService.filterDataByGender(gender);
        } else {
            sortedList = developerService.getAllDeveloper();
        }

        log.debug("Filtered developers count: {}", sortedList.size());
        return new ResponseEntity<>(sortedList, HttpStatus.OK);
    }

    //upload data from excel
    @PostMapping("/upload")
    public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile file) {
        log.info("Uploading Excel file: {}", file.getOriginalFilename());
        if (!GetExcelData.checkExcelFormat(file)) {
            log.error("Invalid file format uploaded: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please Upload Only Excel File");
        }

        try {
            String message = this.developerService.saveExcelFormat(file);
            log.info("Excel data uploaded successfully");
            return ResponseEntity.ok(Map.of("message", message));
        } catch (RuntimeException e) {
            log.error("Error while uploading Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getExcel")
    public ResponseEntity<List<Developer>> excelToDeveloperList() {
        log.info("Get developer list from Excel file");
        List<Developer> developerList = developerService.excelToDeveloperList();

        return new ResponseEntity<>(developerList, HttpStatus.OK);
    }

//    @GetMapping("/download")
//    public ResponseEntity<Resource> databaseToExcel(){
//        // Generate Excel from DB
//        ByteArrayInputStream databaseToExcel = developerService.databaseToExcel();
//        // Wrap it in InputStreamResource
//        InputStreamResource resource = new InputStreamResource(databaseToExcel);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=developerData.xlsx")
//                .contentType(MediaType.parseMediaType(
//                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                .body(resource);
//    }

    @GetMapping("/download/{admin_id}")
    public ResponseEntity<?> databaseToExcel(@PathVariable("admin_id") int admin_id, HttpServletRequest request) {
        try {
            log.info("Admin {} requested to download developer Excel", admin_id);

            ByteArrayInputStream excelStream = developerService.databaseToExcel(admin_id);
            String acceptHeader = request.getHeader("Accept");


            if (acceptHeader != null && acceptHeader.contains("application/json")) {
                byte[] bytes = excelStream.readAllBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);

                log.debug("Excel file converted to Base64 for JSON response");
                Map<String, String> response = new HashMap<>();
                response.put("fileName", "developerData.xlsx");
                response.put("fileData", base64);

                return ResponseEntity.ok(response);
            }

            InputStreamResource resource = new InputStreamResource(excelStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=developerData.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (RuntimeException e) {
            log.warn("Unauthorized access attempt by adminId={} : {}", admin_id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error while downloading Excel: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Something went wrong: " + e.getMessage());
        }
    }
}

