package com.company.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotBlank(message = "Please Enter Your First Name")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First name must start with a capital letter and contain only letters")
    private String fName;

    @NotBlank(message = "Please Enter Your Last Name and First letter must be Capital")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last name must start with a capital letter and contain only letters")
    private String lNAme;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age must not exceed 60")
    private int age;

    @NotBlank(message = "Please Enter Your City")
    private String city;

    @Pattern(regexp = "Male|Female", message = "Please Enter Proper Gender")
    private String gender;

    @Positive(message = "Salary Must Be Greater Than Zero")
    private long salary;

    @Min(value = 1950, message = "Please Enter Your Valid Year of Birth")
    private int YearOfBirth;

    private String developerId;


}
