package com.company.schedular;

import com.company.repositories.DeveloperRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class DeveloperBirthdaySchedular {

    @Autowired
    private DeveloperRepository developerRepository;

    public void updateAgeOnBirthday(){
        log.info("Checking developers with birthdays today...");

        int updatedCount = developerRepository.updateAgeOnBirthday(LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth());

        log.info("Updated {} developers ages.", updatedCount);

    }
}
