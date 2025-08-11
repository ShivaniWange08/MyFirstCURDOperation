package com.company.controller;

import com.company.entity.Developer;
import com.company.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @PostMapping("/addDeveloper")
    public ResponseEntity<String> addDeveloper(@RequestBody Developer developer){
        System.err.println(developer);
        developerService.saveDeveloper(developer);

        String msg =  "Heyy..." + developer.getfName() +", Your profile has been successfully added," + " and Your DeveloperId is : " +developer.getDeveloperId();
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/getAllDeveloper")
    public ResponseEntity<List<Developer>> getAllData(){
       List<Developer> developerList = developerService.getAllDeveloper();
       return new ResponseEntity<>(developerList,HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Developer> getDeveloperById(@PathVariable("id") int id){
        Developer developer = developerService.getDeveloperById(id);
        return new ResponseEntity<>(developer, HttpStatus.OK);

    }
    @DeleteMapping("/deleteDeveloperById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ("id") int id){
        String delete = developerService.deleteDeveloperById(id);
        return new ResponseEntity<>(delete, HttpStatus.OK);
    }

    @PutMapping("/updateDeveloper/{id}")
    public ResponseEntity<Developer> updateDeveloper(@PathVariable ("id") int id, @RequestBody Developer developer){
        Developer updateDeveloper = developerService.updateDeveloper(id, developer);
        return new ResponseEntity<>(updateDeveloper, HttpStatus.OK);
    }

    //addListofDeveloper
    @PostMapping("/listOfDeveloper")
    public ResponseEntity<List<Developer>> savelistOfDeveloper(@RequestBody List<Developer> developerList){
        //List<Developer> developerList1 =
                developerService.savelistOfDeveloper(developerList);
                return new ResponseEntity<>(developerList, HttpStatus.CREATED);
    }

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
}
