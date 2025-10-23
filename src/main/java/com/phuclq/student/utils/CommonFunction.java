package com.phuclq.student.utils;

import com.phuclq.student.domain.School;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommonFunction {

    private static String SCHOOL_PATH = "D:/SPRING/Freelancer/student-server/school.xlsx";

    public static List<School> readSchoolsFromExcel() {
        List<School> schools = new ArrayList<>();
        School school;
        try {
            FileInputStream fis = new FileInputStream(new File(SCHOOL_PATH));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            itr.next();
            while (itr.hasNext()) {
                Row row = itr.next();
                String name = row.getCell(0).getStringCellValue();
                school = new School();
                school.setSchoolName(name);
                schools.add(school);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schools;
    }
}
