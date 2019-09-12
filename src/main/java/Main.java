import features.*;
import model.Articles;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {

//    static private List<Articles> productList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

//        Comparison comparison = new Comparison();
//        comparison.run();

//        Generator modelGenerator = new UpsaleForAccessoryGenerator();
//        modelGenerator.generate();

//        Generator modelGenerator = new RelatedModelGenerator();
//        modelGenerator.generate();


        Generator modelGenerator = new UpsaleGenerator();
        modelGenerator.generate();

//        File file = new File("c:/Users/Admin/Desktop/rel.xlsx");
//        FileInputStream inputStream = new FileInputStream(file);
//        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.rowIterator();
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
////            if(row.getCell(1).getStringCellValue().equals("_MODEL_")) continue;
//            if (row.getCell(4) != null && row.getCell(5) != null) {
//                Articles articles = new Articles(row.getCell(4).getStringCellValue(), row.getCell(5).getStringCellValue());
//                productList.add(articles);
//            }
//        }
//        for (Articles articles : productList) {
//            Iterator<Row> rowIterator1 = sheet.rowIterator();
//            while (rowIterator1.hasNext()) {
//                Row row1 = rowIterator1.next();
//                if(row1.getCell(1).getStringCellValue().equals("_MODEL_")) continue;
//                if (row1.getCell(3).getStringCellValue().contains(articles.getOldArt())) {
//                    String newStr = row1.getCell(3).getStringCellValue().replace(articles.getOldArt(), articles.getNewArt());
//                    XSSFRow row2 = sheet.getRow(row1.getRowNum());
//                    XSSFCell cell = row2.createCell(6);
//                    cell.setCellValue(newStr);
//                }
//            }
//        }
//        System.out.println("0");
//        inputStream.close();
//        FileOutputStream out = new FileOutputStream(file);
//        workbook.write(out);
//        out.close();
    }
}
