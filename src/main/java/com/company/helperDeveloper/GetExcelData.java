package com.company.helperDeveloper;

import com.company.entity.Developer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class GetExcelData {

    public static boolean checkExcelFormat(MultipartFile file) {

        String contentType = file.getContentType();
        return (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

    }

    public static List<Developer> convertExcelToDeveloperList(InputStream inputStream) {
        List<Developer> developerList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            //int rowNumber=0;
            Iterator<Row> iterator = sheet.iterator();
            iterator.next();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                Developer d = new Developer();

                //d.setFName(row.getCell(0).getStringCellValue());
                String fName = getCellValueAsString(row.getCell(0));
                if (fName == null || fName.isBlank()) {
                    throw new IllegalArgumentException("First name is missing at row " + (row.getRowNum() + 1));
                }
                d.setFName(fName);

                //d.setLNAme(row.getCell(1).getStringCellValue());
                String lName = getCellValueAsString(row.getCell(1));
                if (lName == null || lName.isBlank()) {
                    throw new IllegalArgumentException("Last name is missing at row " + (row.getRowNum() + 1));
                }
                d.setLNAme(lName);

                //d.setAge((int) row.getCell(2).getNumericCellValue());
//                int age = Period.between(dob, LocalDate.now()).getYears();
//                if (age < 18 || age > 60) {
//                    throw new IllegalArgumentException("Invalid age (" + age + ") at row " + (row.getRowNum() + 1));
//                }
//                d.setAge(age);

                //d.setCity(row.getCell(3).getStringCellValue());
                String city = getCellValueAsString(row.getCell(2));
                if (city == null || city.isBlank()) {
                    throw new IllegalArgumentException("City is missing at row " + (row.getRowNum() + 1));
                }
                d.setCity(city);

                //d.setGender(row.getCell(4).getStringCellValue());
                String gender = getCellValueAsString(row.getCell(3));
                if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female") && !gender.equalsIgnoreCase("Other")) {
                    throw new IllegalArgumentException(" Invalid gender at row " + (row.getRowNum() + 1) + ". Must be Male, Female, or Other.");
                }
                d.setGender(gender);

                //d.setSalary((long) row.getCell(5).getNumericCellValue());
                long salary = (long) row.getCell(4).getNumericCellValue();
                if (salary <= 0) {
                    throw new IllegalArgumentException(" Salary must be greater than 0 at row " + (row.getRowNum() + 1));
                }
                d.setSalary(salary);

                //d.setYearOfBirth((int) row.getCell(6).getNumericCellValue());
                int yob = (int) row.getCell(5).getNumericCellValue();
                if (yob < 1950 || yob > 2025) {
                    throw new IllegalArgumentException("Invalid Year of Birth at row " + (row.getRowNum() + 1));
                }
                d.setYearOfBirth(yob);
                // DOB (Excel format: dd-MM-yyyy)
                String dobStr = getCellValueAsString(row.getCell(6));
                try {
                    LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    d.setDob(dob);
                    // calculate age from dob
                    int calculatedAge = Period.between(dob, LocalDate.now()).getYears();
                    d.setAge(calculatedAge);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid DOB format at row " + (row.getRowNum() + 1) + ". Expected format: dd-MM-yyyy");
                }


                developerList.add(d);
            }

        } catch (Exception e) {
            throw new RuntimeException(" " + e.getMessage());
        }
        return developerList;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue());
                }
            default:
                return "";
        }
    }
}