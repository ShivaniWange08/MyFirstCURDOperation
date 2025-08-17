package com.company.service;

import com.company.entity.Developer;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface DeveloperService {

    String saveDeveloper(Developer developer);

    List<Developer> getAllDeveloper() ;

    Developer getDeveloperById(int id);

    String deleteDeveloperById(int id);

    Developer updateDeveloper(int id, Developer newDeveloper);

    List<Developer> savelistOfDeveloper(List<Developer> developerList);

    List<Developer> filterDataByCity(String city);

    List<Developer> filterDataByGender(String gender);

    List<Developer> filterByCityAndGender(String city, String gender);

    String saveExcelFormat(MultipartFile file);

    List<Developer> excelToDeveloperList();

    ByteArrayInputStream databaseToExcel(int adminId)throws IOException, GeneralSecurityException;
}
