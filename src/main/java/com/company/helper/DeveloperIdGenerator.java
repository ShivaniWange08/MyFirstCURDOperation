package com.company.helper;

import com.company.entity.Developer;

public class DeveloperIdGenerator {

    public static String generatedDeveloperId(Developer developer){
        String fName = developer.getfName();
        String lName = developer.getlNAme();
        int YOB = developer.getYearOfBirth();

        char a = lName.charAt(0);
        int  b = YOB % 100;

        String userName = a+fName+b;
        System.err.println("username is " +userName);
        return userName;
    }
}
