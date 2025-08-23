package com.company.repositories;

import com.company.entity.Developer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Integer> {

    @Query("SELECT d FROM Developer d WHERE d.age= :age")
    List<Developer> findByAge(@Param("age") int age);

    @Query(value = "SELECT * FROM Developer",nativeQuery = true)
    List<Developer> findAllDeveloper();

    @Modifying
    @Transactional
    @Query(value = "UPDATE developer SET developer_id = :developerId WHERE id = :id", nativeQuery = true)
    void updateDeveloperId(@Param("id") int id, @Param("developerId") String developerId);

    @Query(value = "SELECT * FROM developer WHERE developer_id IS NULL OR developer_id = ''", nativeQuery = true)
    List<Developer> findDeveloperWithMissingId();

    @Modifying
    @Transactional
    @Query("UPDATE Developer d SET d.age = d.age + 1 " +
            "WHERE MONTH(d.dob) = :month AND DAY(d.dob) = :day")
    int updateAgeOnBirthday(@Param("month") int month, @Param("day") int day);

}



