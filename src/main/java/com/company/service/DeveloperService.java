package com.company.service;

import com.company.entity.Developer;

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
}
