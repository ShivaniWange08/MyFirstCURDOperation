package com.company.schedular;

import com.company.entity.Developer;
import com.company.helperDeveloper.DeveloperIdGenerator;
import com.company.repositories.DeveloperRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DeveloperIdSchedular {

    @Autowired
    private DeveloperRepository developerRepository;

//    public DeveloperIdSchedular(DeveloperRepository developerRepository){
//        this.developerRepository = developerRepository;
//    }

    @Scheduled(fixedRate = 60000) // runs every 1 minute
    public void checkAndGenerateMissingDeveloperId(){
        List<Developer> id = developerRepository.findDeveloperWithMissingId();

        if(id.isEmpty()){
            log.info("No missing developer id found in db");
            return;
        }
        log.info("Found {} developers with missing IDs. Generating...", id.size());

        for(Developer d : id){
            try{
                String newId = DeveloperIdGenerator.generatedDeveloperId(d);
                developerRepository.updateDeveloperId(d.getId(), newId);
                log.info("Updated Developer {} with DeveloperId: {}", d.getId(), newId);

            } catch (Exception e) {
                log.error("Failed to generate DeveloperId for Developer {}: {}", d.getId(), e.getMessage());
            }
        }
    }
}
