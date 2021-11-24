package it.academy.by.befitapp.utils;

import it.academy.by.befitapp.dao.api.IProductDao;
import it.academy.by.befitapp.model.Product;
import it.academy.by.befitapp.service.api.IProductService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Component
public class ProductBaseExelParser {
    private final IProductService iProductService;

    public ProductBaseExelParser(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    public void parseXlsFileToDB(String fileName) {
        try (InputStream inputStream = new FileInputStream(fileName);
             HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                Product product = new Product();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    CellType cellType = cell.getCellType();
                    Integer columnIndex = cell.getColumnIndex();
                    if (columnIndex.equals(0) && cellType.equals(CellType.STRING)) {
                        product.setName(cell.getStringCellValue());
                    }
                    if (columnIndex.equals(1) && cellType.equals(CellType.NUMERIC)) {
                        product.setProtein(cell.getNumericCellValue());
                    }
                    if (columnIndex.equals(2) && cellType.equals(CellType.NUMERIC)) {
                        product.setFat(cell.getNumericCellValue());
                    }
                    if (columnIndex.equals(3) && cellType.equals(CellType.NUMERIC)) {
                        product.setCarbohydrates(cell.getNumericCellValue());
                    }
                    if (columnIndex.equals(4) && cellType.equals(CellType.NUMERIC)) {
                        product.setCalories(cell.getNumericCellValue());
                    }
                }
                product.setWeight(100.00);
                this.iProductService.save(product);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
