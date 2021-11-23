package it.academy.by.befitapp.utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ProductBaseExelParser {
    private static String FILE_NAME = "Calorie_table.xls";

    public static String parse(String FILE_NAME) {
        String result = "";
        InputStream inputStream = null;
        HSSFWorkbook workbook = null;
        try {
            inputStream = new FileInputStream(FILE_NAME);
            workbook = new HSSFWorkbook(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()){
            Row row = iterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                CellType cellType = cell.getCellType();
                Integer columnIndex = cell.getColumnIndex();
            }
        }
        return result;
    }
}
