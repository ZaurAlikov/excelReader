package features;

import model.Product;
import org.apache.poi.ss.usermodel.Cell;
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
import java.util.*;

public class UpsaleGenerator implements Generator {

    private List<Product> productList = new ArrayList<>();
    private Map<String, Product> forGruzKorzMap = new HashMap<>();
    private Map<String, Product> forBoxesMap = new HashMap<>();
    private Map<String, Product> forVeloMap = new HashMap<>();
    private Map<String, Product> forSnowMap = new HashMap<>();
    private Map<String, Product> forBagAccessories = new HashMap<>();
    private Map<String, Product> forTouristsBag = new HashMap<>();
    private Map<String, Product> forRoadBag = new HashMap<>();
    private int countUpsale = 6;

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
            Product product = new Product(row.getCell(0).getStringCellValue(), new DataFormatter().formatCellValue(row.getCell(2)), row.getCell(1).getStringCellValue());
            productList.add(product);
        }

        XSSFSheet upsaleSheet = workbook.getSheetAt(5);
        Iterator<Row> upsaleRowIterator = upsaleSheet.rowIterator();
        while (upsaleRowIterator.hasNext()) {
            Row row = upsaleRowIterator.next();
            if(row.getCell(1) != null && row.getCell(1).getStringCellValue().equals("Боксы")) continue;
            fillMap(0, forGruzKorzMap, row);
            fillMap(1, forBoxesMap, row);
            fillMap(2, forVeloMap, row);
            fillMap(3, forSnowMap, row);
            fillMap(5, forBagAccessories, row);
            fillMap(6, forTouristsBag, row);
            fillMap(8, forRoadBag, row);
        }

        Iterator<Row> mainRowIterator = sheet.rowIterator();
        while (mainRowIterator.hasNext()) {
            Row row = mainRowIterator.next();
            if(row.getCell(1).getStringCellValue().equals("_MODEL_")) continue;
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("03") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forGruzKorzMap, sheet, row);
            }
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("04") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forBoxesMap, sheet, row);
            }
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("08") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forVeloMap, sheet, row);
            }
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("07") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forSnowMap, sheet, row);
            }
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("19") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forBagAccessories, sheet, row);
            }
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("12") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forTouristsBag, sheet, row);
            }
            if (row.getCell(1).getStringCellValue().substring(3,5).equals("10") && !isFullList(row.getCell(3), countUpsale)) {
                writeUpsale(forRoadBag, sheet, row);
            }
        }
        inputStream.close();
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
    }

    private void fillMap(int col, Map<String, Product> map, Row row) {
        if (row.getCell(col) != null && !row.getCell(col).getStringCellValue().equals("")) {
            for (Product product : productList) {
                if (product.getModel().equals(row.getCell(col).getStringCellValue())) {
                    map.put(row.getCell(col).getStringCellValue(), product);
                    break;
                }
            }
        }
    }

    private void writeUpsale(Map<String, Product> productMap, XSSFSheet sheet, Row row) {
        List<String> sku = new ArrayList<>();
        productMap.forEach((key, value) -> sku.add(value.getSku()));
//        if (row.getCell(0).getStringCellValue().contains("Thule")) {
//            sku.add("450400");
//        }
//        if (row.getCell(1).getStringCellValue().substring(3,5).equals("04") && row.getCell(0).getStringCellValue().contains("LUX")) {
//            sku.add("03-06-002671");
//        }
        if (sku.size() > 0) {
            StringBuilder upsale = new StringBuilder();
            int rndm;
            Random r = new Random();
            do {
                if (getUpsales(upsale).size() == 1 && getUpsales(upsale).get(0).equals("")) {
                    rndm = r.nextInt(((sku.size() - 1)) + 1);
                    upsale.append(sku.get(rndm));
                } else {
                    do {
                        rndm = r.nextInt(((sku.size() - 1)) + 1);
                    } while (getUpsales(upsale).contains(sku.get(rndm)));
                    upsale.append(",").append(sku.get(rndm));
                }
            } while (getCountUpsale(upsale) < sku.size() && getCountUpsale(upsale) < countUpsale);
            XSSFRow row1 = sheet.getRow(row.getRowNum());
            XSSFCell cell = row1.createCell(3);
            cell.setCellValue(upsale.toString());
        }
    }

    private int getCountUpsale(StringBuilder upsale) {
        return upsale.toString().split(",").length;
    }

    private List<String> getUpsales(StringBuilder upsale) {
        return Arrays.asList(upsale.toString().split(","));
    }

    private boolean isFullList(Cell cell, int countUpsale) {
        String stringCellValue;
        if (cell == null) return false;
        if (cell.getCellType() == CellType.NUMERIC) {
            stringCellValue = String.valueOf(cell.getNumericCellValue());
        } else {
            stringCellValue = cell.getStringCellValue();
        }
        String[] split = stringCellValue.split(",");
        return split.length >= countUpsale;
    }
}
