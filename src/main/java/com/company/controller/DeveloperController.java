package com.company.controller;

import com.company.entity.Developer;
import com.company.helper.GetExcelData;
import com.company.service.DeveloperService;
import jakarta.validation.Valid;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    //to add developer
    @PostMapping("/addDeveloper")
    public ResponseEntity<String> addDeveloper(@Valid @RequestBody Developer developer){
        System.err.println(developer);
        developerService.saveDeveloper(developer);

        String msg =  "Heyy..." + developer.getFName() +", Your profile has been successfully added," + " and Your DeveloperId is : " +developer.getDeveloperId();
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    //to get all developer
    @GetMapping("/getAllDeveloper")
    public ResponseEntity<List<Developer>> getAllData(){
       List<Developer> developerList = developerService.getAllDeveloper();
       return new ResponseEntity<>(developerList,HttpStatus.OK);
    }

    //to get developer by id
    @GetMapping("/getById/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable("id") int id){
        Developer developer = developerService.getDeveloperById(id);
        return new ResponseEntity<>(developer, HttpStatus.OK);

    }

    //to delete developer by id
    @DeleteMapping("/deleteDeveloperById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ("id") int id){
        String delete = developerService.deleteDeveloperById(id);
        return new ResponseEntity<>(delete, HttpStatus.OK);
    }

    //to update developer by id
    @PutMapping("/updateDeveloper/{id}")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable ("id") int id, @RequestBody Developer developer){
        Developer updateDeveloper = developerService.updateDeveloper(id, developer);
        return new ResponseEntity<>(updateDeveloper, HttpStatus.OK);
    }

    //addListofDeveloper
    @PostMapping("/listOfDeveloper")
    public ResponseEntity<List<Developer>> savelistOfDeveloper(@Valid @RequestBody List<Developer> developerList){
        //List<Developer> developerList1 =
                developerService.savelistOfDeveloper(developerList);
                return new ResponseEntity<>(developerList, HttpStatus.CREATED);
    }

    //search developer according city, gender
    @GetMapping("/filter")
    public ResponseEntity<List<Developer>> filterDeveloper(@RequestParam (required = false) String city,
                                                           @RequestParam (required = false) String gender){
        List<Developer> sortedList = new ArrayList<>();
        if((gender != null) && (city != null)){
            sortedList = developerService.filterByCityAndGender(city, gender);
        }else if(city != null){
            sortedList =  developerService.filterDataByCity(city);
        }else if(gender != null){
            sortedList = developerService.filterDataByGender(gender);
        }else{
            sortedList = developerService.getAllDeveloper();
        }

        return new ResponseEntity<>(sortedList, HttpStatus.OK);
    }

    //upload data from excel
    @PostMapping("/upload")
    public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile file){
        if (!GetExcelData.checkExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please Upload Only Excel File");
        }

        try {
            String message = this.developerService.saveExcelFormat(file);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getExcel")
    public ResponseEntity<List<Developer>> excelToDeveloperList(){
        List<Developer> developerList = developerService.excelToDeveloperList();

        return new ResponseEntity<>(developerList, HttpStatus.OK);
    }
}
