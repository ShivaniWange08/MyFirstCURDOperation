package com.company.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_id")
    @SequenceGenerator(name = "admin_id", sequenceName = "admin_id", initialValue = 10001, allocationSize = 1)
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;
    private String fName;
    private String lName;
    private String designation;
    private long MobNumber;
}
