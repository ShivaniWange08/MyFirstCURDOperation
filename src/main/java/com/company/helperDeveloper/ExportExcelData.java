package com.company.helperDeveloper;

import com.company.entity.Developer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
public class ExportExcelData {

    public static final String[] HEADERS={
            "id", "fName", "LName", "age", "city", "gender", "salary", "YearOfBirth", "dob", "developerId"
    };

    public static String SHEET_NAME = "developerExcel_data";

    public static ByteArrayInputStream databasetoExcel(List<Developer> developerList) throws IOException, GeneralSecurityException {
        //create workbbok
        Workbook workbook = new XSSFWorkbook();
        //ByteArrayOutputStream out = new ByteArrayOutputStream();

        try{
            log.info("Starting Excel export for {} developers", developerList.size());
            //create sheet
           Sheet sheet = workbook.createSheet(SHEET_NAME);

           //create row
            Row row = sheet.createRow(0);
            for(int i=0; i < HEADERS.length; i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }
            //value rows
            int rowIndex=1;
            for(Developer d : developerList){
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;

                dataRow.createCell(0).setCellValue(d.getId());
                dataRow.createCell(1).setCellValue(d.getFName());
                dataRow.createCell(2).setCellValue(d.getLNAme());
                dataRow.createCell(3).setCellValue(d.getAge());
                dataRow.createCell(4).setCellValue(d.getCity());
                dataRow.createCell(5).setCellValue(d.getGender());
                dataRow.createCell(6).setCellValue(d.getSalary());
                dataRow.createCell(7).setCellValue(d.getYearOfBirth());
                dataRow.createCell(8).setCellValue(d.getDob() != null? d.getDob().toString() : "");
                dataRow.createCell(9).setCellValue(d.getDeveloperId());
            }
            // to auto-size column
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Save workbook to a temp file
            File tempFile = File.createTempFile("developerData", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }
            log.info("Excel file created successfully at: {}", tempFile.getAbsolutePath());

            // ======== Password Protection ========
            String password = "root"; // <-- change this as needed
            POIFSFileSystem fs = new POIFSFileSystem();
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor encryptor = info.getEncryptor();
            encryptor.confirmPassword(password);

            encryptor.confirmPassword(password);
            try (OPCPackage opc = OPCPackage.open(tempFile, PackageAccess.READ_WRITE);
                 OutputStream os = encryptor.getDataStream(fs)) {
                opc.save(os);
            } catch (InvalidFormatException e) {
                log.error("Invalid format while encrypting Excel", e);
                throw new RuntimeException(e);
            }
            // Write encrypted file to byte array
            ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
            fs.writeFilesystem(encryptedOut);

            log.info("Excel export completed with password protection");
            return new ByteArrayInputStream(encryptedOut.toByteArray());


        }catch (IOException | GeneralSecurityException e) {
            log.error("Failed to export and protect Excel", e);
            throw new RuntimeException("Failed to export and protect Excel", e);
        } finally {
            workbook.close();
        }


    }
}
