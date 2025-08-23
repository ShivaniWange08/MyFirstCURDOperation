package com.company.schedular;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class SimpleSchedular {

    //Task 1
    // create a schedular to check if developer_id is missing
    //if it is missing then create again and store it in database
    //use native wuery to update field


    //Task -2
    // add column - DOB - dataype - localDateTime
    //clear databse and add new field
    //age should not be taken from postman[JSON],
    //age should be calculated from dob and stored in db
    //create a schedular to update age by 1 if today is DOB of developer & store it in db
    //use custom JPQL querry to update




    @Scheduled(fixedDelay = 5000)
    public void printSomething(){
        System.err.println("today is thursday");
    }

    @Scheduled(cron = "0 * * * * ?\n")
    public void printSomething2(){
        System.err.println("today is good day");
    }
}
