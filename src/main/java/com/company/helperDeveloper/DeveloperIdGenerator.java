package com.company.helperDeveloper;

import com.company.entity.Developer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeveloperIdGenerator {

    public static String generatedDeveloperId(Developer developer){
        String fName = developer.getFName();
        String lName = developer.getLNAme();
        int YOB = developer.getYearOfBirth();

        if (lName == null || lName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        char a = lName.charAt(0);
        int  b = YOB % 100;

        String userName = a+fName+b;
        log.info("Generated DeveloperId is {}", userName);
        return userName;
    }
}
