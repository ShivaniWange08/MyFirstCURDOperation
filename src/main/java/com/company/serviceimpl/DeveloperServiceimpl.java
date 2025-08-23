package com.company.serviceimpl;

import com.company.entity.Admin;
import com.company.entity.Developer;
import com.company.exception.DeveloperNotFoundException;
import com.company.helperDeveloper.DeveloperIdGenerator;
import com.company.helperDeveloper.ExportExcelData;
import com.company.helperDeveloper.GetExcelData;
import com.company.repositories.AdminRepository;
import com.company.repositories.DeveloperRepository;
import com.company.service.DeveloperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

@Service
@Slf4j
public class DeveloperServiceimpl implements DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public String saveDeveloper(Developer developer) {
        //Generate DeveloperId
        String developerId = DeveloperIdGenerator.generatedDeveloperId(developer);
        log.info("Generated DeveloperId: {}", developerId);
        developer.setDeveloperId(developerId);

        //Calculate Age from DOB
        if(developer.getDob() != null){
            int calculatedAge = Period.between(developer.getDob(), LocalDate.now()).getYears();
            developer.setAge(calculatedAge);
        }
        else {
            log.warn("DOB not provided for Developer: {}, Age not calculated", developer.getFName());
        }

        Developer savedDeveloper = developerRepository.save(developer);

        return "developer saved";
    }

    @Override
    public List<Developer> getAllDeveloper() {
        List<Developer> developerList = developerRepository.findAll();
        log.info("list of{} developers", developerList.size());
        return developerList;
    }

    @Override
    public Developer getDeveloperById(int id) {
        Developer developer =  developerRepository.findById(id).orElseThrow(() -> new DeveloperNotFoundException("Developer with id not found " + id));
        log.info("Developer with id {}: {}", id, developer);
        return developer;
    }

    @Override
    public String deleteDeveloperById(int id) {
        developerRepository.deleteById(id);
        log.warn("Developer with id {} deleted", id);
        return "Developer Deleted";
    }

    @Override
    public Developer updateDeveloper(int id, Developer newDeveloper) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(()-> new NullPointerException("Developer with id are not found in database" +id));
        log.info("old developer from db {}", developer);
        log.info("Developer object with value to be updated {} ", newDeveloper);
        developer.setFName(newDeveloper.getFName());
        developer.setLNAme(newDeveloper.getLNAme());
        //developer.setAge(newDeveloper.getAge());
        developer.setCity(newDeveloper.getCity());
        developer.setSalary(newDeveloper.getSalary());
        developer.setDob(newDeveloper.getDob());

        Developer updateDeveloper = developerRepository.save(developer);
        log.info("Developer with updated value {}", updateDeveloper);
        return updateDeveloper;
    }

    @Override
    public List<Developer> savelistOfDeveloper(List<Developer> developerList) {
        for(Developer developer : developerList){
            //Generate DeveloperId
            String developerId = DeveloperIdGenerator.generatedDeveloperId(developer);
            developer.setDeveloperId(developerId);

            //calculate age from dob
            if(developer.getDob() != null){
                int culAge = Period.between(developer.getDob(), LocalDate.now()).getYears();
                developer.setAge(culAge);
            }
        }

        List<Developer> savedDevelopers = developerRepository.saveAll(developerList);
                log.info("{} Developer List :", developerList.size());
        return savedDevelopers;
    }

    @Override
    public List<Developer> filterDataByCity(String city) {
        List<Developer> developerList = developerRepository.findAll();
        List<Developer> filteredList = developerList.stream()
                .filter(developer-> developer.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
        log.info("Filtered {} developers by city {}", filteredList.size(),city);
        return filteredList;
    }

    @Override
    public List<Developer> filterDataByGender(String gender) {
        List<Developer> developerList = developerRepository.findAll();
        List<Developer> filteredList = developerList.stream()
                .filter(developer-> developer.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
        log.info("Filtered {} developer by gender{}", filteredList.size(),gender);
        return filteredList;
    }

    @Override
    public List<Developer> filterByCityAndGender(String city, String gender) {
        List<Developer> developerList = developerRepository.findAll();
        List<Developer> filteredList = developerList.stream()
                .filter(developer -> developer.getCity().equalsIgnoreCase(city))
                .filter(developer -> developer.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
        log.info("Filtered {} developers by city {} and gender {}", filteredList.size(), city, gender);
        return filteredList;
    }

    //check file is of excel type or not


    @Override
    public String saveExcelFormat(MultipartFile file) {
        try {
          List<Developer> developers = GetExcelData.convertExcelToDeveloperList(file.getInputStream());
            log.info("Developers read from Excel: {}", developers.size());
            developers.forEach(System.out::println);
            for (Developer d : developers) {
                d.setDeveloperId(DeveloperIdGenerator.generatedDeveloperId(d));
                log.debug("Generated DeveloperId for {}: {}", d.getFName(), d.getDeveloperId());
            }
          developerRepository.saveAll(developers);
            log.info("Excel data saved successfully");
            return "Excel data saved successfully";
        } catch (RuntimeException e) { // Catch validation errors
            log.error("Excel Validation Error: {}", e.getMessage(), e);
            return "Excel Validation Error: " + e.getMessage();
        } catch (IOException e) {
            log.error("Failed to read Excel file: {}", e.getMessage(), e);
            return "Failed to read Excel file: " + e.getMessage();
        }
    }

    @Override
    public List<Developer> excelToDeveloperList() {
     List<Developer> developerList = developerRepository.findAll();
        log.info("Get {} developers for Excel export", developerList.size());
     return developerList;
    }

    @Override
    public ByteArrayInputStream databaseToExcel(int admin_id) throws IOException, GeneralSecurityException {

        try{
            Optional<Admin> adminOptional = adminRepository.findById(admin_id);
            if (adminOptional.isEmpty()){
                log.warn("Unauthorized access attempt by adminId {}", admin_id);
                throw new RuntimeException("You don't have access!!");
            }
            List<Developer> developerList = developerRepository.findAll();
            log.info("Admin {} downloaded Excel with {} developers", admin_id, developerList.size());
            return ExportExcelData.databasetoExcel(developerList);
        } catch (IOException e){
            log.error("Failed to export data to Excel", e);
            throw new RuntimeException("Failed to export data to Excel", e);
        }catch (GeneralSecurityException e){
            log.error("Excel security error", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Developer> getDeveloperByAge(int age) {
        List<Developer> developerByAge =  developerRepository.findByAge(age);
        return developerByAge;

    }
}
