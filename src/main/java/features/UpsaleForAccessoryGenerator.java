package features;

import model.Product;
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
import java.util.*;

public class UpsaleForAccessoryGenerator implements Generator {

    private List<Product> productList = new ArrayList<>();
    private List<String> forAccessoryList = Arrays.asList("841","522-1","538","800603","ME 784000","ME 782000","323","986","551","8568");


    @Override
    public void generate() throws IOException {
        File file = new File("c:/Users/Admin/Desktop/rel1.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(4);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(row.getCell(1).getStringCellValue().equals("_MODEL_")) continue;
            Product product;
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("06")) {
                product = new Product(row.getCell(0).getStringCellValue(), new DataFormatter().formatCellValue(row.getCell(2)), row.getCell(1).getStringCellValue());
                productList.add(product);
            }
        }

        List<String> intList = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
        List<String> triples = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            triples.add(generateInt(intList, triples));
        }
        int tripleNum = 0;
        Iterator<Row> mainRowIterator = sheet.rowIterator();
        while (mainRowIterator.hasNext()) {
            Row row = mainRowIterator.next();
            if(row.getCell(1).getStringCellValue().equals("_MODEL_")) continue;
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("06")) {
                StringBuilder resultUpsales = new StringBuilder();
                List<String> trpLst = Arrays.asList(triples.get(tripleNum).split(","));
                trpLst.forEach(t -> {
                    if (getUpsales(resultUpsales).size() == 1 && getUpsales(resultUpsales).get(0).equals("")) {
                        resultUpsales.append(forAccessoryList.get(Integer.valueOf(t)));
                    } else {
                        resultUpsales.append(",").append(forAccessoryList.get(Integer.valueOf(t)));
                    }
                });
                XSSFRow row1 = sheet.getRow(row.getRowNum());
                XSSFCell cell = row1.createCell(3);
                cell.setCellValue(resultUpsales.toString());
                ++tripleNum;
            }
        }
        inputStream.close();
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
    }

    private String generateInt(List<String> intList, List<String> upsales) {
        StringBuilder upsale = new StringBuilder();
        int countUpsale = 3;
        int rndm;
        boolean repeat;
        Random r = new Random();
        do {
            do {
                if (getUpsales(upsale).size() == 1 && getUpsales(upsale).get(0).equals("")) {
                    rndm = r.nextInt(((intList.size() - 1)) + 1);
                    upsale.append(intList.get(rndm));
                } else {
                    do {
                        rndm = r.nextInt(((intList.size() - 1)) + 1);
                    } while (getUpsales(upsale).contains(intList.get(rndm)));
                    upsale.append(",").append(intList.get(rndm));
                }
            } while (getCountUpsale(upsale) < intList.size() && getCountUpsale(upsale) < countUpsale);
            if (upsales.contains(upsale.toString())) {
                repeat = true;
                upsale = new StringBuilder();
            } else {
                repeat =false;
            }
        } while (repeat);
        if (upsales.contains(upsale.toString())) {
            System.out.println("Повтор");
        }
        System.out.println(upsale.toString());
        return upsale.toString();
    }

    private int getCountUpsale(StringBuilder upsale) {
        return upsale.toString().split(",").length;
    }

    private List<String> getUpsales(StringBuilder upsale) {
        return Arrays.asList(upsale.toString().split(","));
    }

}
