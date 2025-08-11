package com.company.serviceimpl;

import com.company.entity.Developer;
import com.company.helper.DeveloperIdGenerator;
import com.company.repositories.DeveloperRepository;
import com.company.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceimpl implements DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    @Override
    public String saveDeveloper(Developer developer) {

        String developerId = DeveloperIdGenerator.generatedDeveloperId(developer);
        System.err.println(developerId);
        developer.setDeveloperId(developerId);

        Developer savedDeveloper = developerRepository.save(developer);

        return "developer saved";


    }

    @Override
    public List<Developer> getAllDeveloper() {
        List<Developer> developerList = developerRepository.findAll();
        return developerList;
    }

    @Override
    public Developer getDeveloperById(int id) {
     Developer developer = developerRepository.findById(id).orElseThrow(() -> new NullPointerException("Developer with id not found" +id));
        return developer;
    }

    @Override
    public String deleteDeveloperById(int id) {
        developerRepository.deleteById(id);
        return "Developer Deleted";
    }

    @Override
    public Developer updateDeveloper(int id, Developer newDeveloper) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(()-> new NullPointerException("Developer with id are not found in database" +id));
        System.err.println("old developer from db " +developer);
        System.err.println("Developer object with value to be updated " +newDeveloper);
        developer.setfName(newDeveloper.getfName());
        developer.setlNAme(newDeveloper.getlNAme());
        developer.setAge(newDeveloper.getAge());
        developer.setCity(newDeveloper.getCity());
        developer.setSalary(newDeveloper.getSalary());

        Developer updateDeveloper = developerRepository.save(developer);
        System.err.println("Developer with updated value "+updateDeveloper);
        return updateDeveloper;
    }

    @Override
    public List<Developer> savelistOfDeveloper(List<Developer> developerList) {
        //List<Developer> developerList1 =
                developerRepository.saveAll(developerList);
        return developerList;
    }

    @Override
    public List<Developer> filterDataByCity(String city) {
        List<Developer> developerList = developerRepository.findAll();
        List<Developer> filteredList = developerList.stream()
                .filter(developer-> developer.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public List<Developer> filterDataByGender(String gender) {
        List<Developer> developerList = developerRepository.findAll();
        List<Developer> filteredList = developerList.stream()
                .filter(developer-> developer.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public List<Developer> filterByCityAndGender(String city, String gender) {
        List<Developer> developerList = developerRepository.findAll();
        List<Developer> filteredList = developerList.stream()
                .filter(developer -> developer.getCity().equalsIgnoreCase(city))
                .filter(developer -> developer.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
        return filteredList;
    }
}
