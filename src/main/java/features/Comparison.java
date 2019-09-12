package features;

import model.Product;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Comparison {

    private List<Product> productList = new ArrayList<>();

    public void run() throws IOException {
        File file = new File("c:/Users/Admin/Desktop/Сопоставление.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(row.getCell(1) == null || row.getCell(1).getStringCellValue().equals("_CATEGORY_")) continue;
            row.getCell(0).setCellType(CellType.STRING);
            Product product = new Product(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue(),  row.getCell(3).getNumericCellValue(), row.getCell(4).getStringCellValue());
            productList.add(product);
        }

        XSSFSheet sheet1 = workbook.getSheetAt(4);
//        Iterator<Row> rowIterator1 = sheet1.rowIterator();
//        while (rowIterator1.hasNext()) {
//            Row row = rowIterator1.next();
//            if(row.getCell(1) == null || row.getCell(1).getStringCellValue().equals("_CATEGORY_")) continue;
//            row.getCell(0).setCellType(CellType.STRING);
//            int i = 0;
//            for (Product product : productList) {
//                if (normalize(row.getCell(0).getStringCellValue()).equals(normalize(product.getSku()))) {
//                    XSSFRow row1 = sheet1.getRow(row.getRowNum());
//                    XSSFCell cell = row1.createCell(4);
//                    cell.setCellValue(product.getModel());
//                    i += 1;
//                }
//            }
//            if (i > 1) {
//                System.out.println(i + " - " + row.getCell(0).getStringCellValue());
//            }
//        }
        Iterator<Row> rowIterator1 = sheet1.rowIterator();
        List<String> artNotMy = new ArrayList<>();

        while (rowIterator1.hasNext()) {
            Row row = rowIterator1.next();
            artNotMy.add(normalize(row.getCell(2).getStringCellValue()));
//            if(row.getCell(1) == null || row.getCell(1).getStringCellValue().equals("_CATEGORY_")) continue;
            row.getCell(2).setCellType(CellType.STRING);
            int i = 0;
            for (Product product : productList) {
                if (normalize(row.getCell(2).getStringCellValue()).equals(normalize(product.getSku()))) {
                    XSSFRow row1 = sheet1.getRow(row.getRowNum());
                    XSSFCell cell = row1.createCell(7);
                    cell.setCellValue(product.getModel());
                    i += 1;
                }
            }
            if (i > 1) {
                System.out.println(i + " - " + row.getCell(0).getStringCellValue());
            }
        }

        productList.forEach(a -> {
            if (!artNotMy.contains(normalize(a.getSku()))) {
                System.out.println(a.getModel() + ";" + a.getSku() + ";" + a.getName());
            }
        });

        inputStream.close();
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
    }

    String normalize(String str) {
        if (str.length() == 3) {
            str = str.concat("000");
        }
        if (str.length() == 4) {
            str = str.concat("00");
        }
        if (str.length() == 5) {
            str = str.concat("0");
        }
        return str;
    }



}
