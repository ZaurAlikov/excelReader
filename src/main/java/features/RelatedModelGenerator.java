package features;

import model.Product;
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

public class RelatedModelGenerator implements Generator {

    private List<Product> productList = new ArrayList<>();

    @Override
    public void generate() throws IOException {
        File file = new File("c:/Users/Admin/Desktop/rel.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(row.getCell(1).getStringCellValue().equals("_MODEL_")) continue;
            Product product = new Product(row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(), row.getCell(2).getNumericCellValue());
            productList.add(product);
        }

        Iterator<Row> mainRowIterator = sheet.rowIterator();
        while (mainRowIterator.hasNext()) {
            Row row = mainRowIterator.next();
            if(row.getCell(1).getStringCellValue().equals("_MODEL_")) continue;

            //количество сопутствующих товаров
            int count = 4;

            List<Product> sorted = sortModel(row.getCell(1).getStringCellValue().substring(4, 5), row.getCell(0).getStringCellValue());
            List<String> models = overPrice(sorted, row.getCell(2).getNumericCellValue(), row.getCell(1).getStringCellValue(), count);
            if (models.size() > 0) {
//                StringBuilder relatedModels = new StringBuilder();
//                int count = models.size() > 3 ? 3 : models.size();
//                int rnd1;
//                int rnd2 = -1;
//                int rnd3;
//                Random r = new Random();
//                rnd1 = r.nextInt(((models.size() - 1)) + 1);
//                relatedModels.append(models.get(rnd1));
//                if (count == 2 || count == 3) {
//                    do {
//                        rnd2 = r.nextInt(((models.size() - 1)) + 1);
//                    } while (rnd1 == rnd2);
//                    relatedModels.append(",").append(models.get(rnd2));
//                }
//                if (count == 3) {
//                    do {
//                        rnd3 = r.nextInt(((models.size() - 1)) + 1);
//                    } while (rnd3 == rnd1 || rnd3 == rnd2);
//                    relatedModels.append(",").append(models.get(rnd3));
//                }
                StringBuilder relatedModels = new StringBuilder();
                int rndm;
                Random r = new Random();
                do {
                    if (getUpsales(relatedModels).size() == 1 && getUpsales(relatedModels).get(0).equals("")) {
                        rndm = r.nextInt(((models.size() - 1)) + 1);
                        relatedModels.append(models.get(rndm));
                    } else {
                        do {
                            rndm = r.nextInt(((models.size() - 1)) + 1);
                        } while (getUpsales(relatedModels).contains(models.get(rndm)));
                        relatedModels.append(",").append(models.get(rndm));
                    }
                } while (getCountUpsale(relatedModels) < models.size() && getCountUpsale(relatedModels) < count);
                XSSFRow row1 = sheet.getRow(row.getRowNum());
                if (row1.getCell(3) != null && !row1.getCell(3).getStringCellValue().equals("")) {
                    continue;
                }
                XSSFCell cell = row1.createCell(3);
                cell.setCellValue(relatedModels.toString());
            }
        }
        inputStream.close();
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
    }

    private int getCountUpsale(StringBuilder upsale) {
        return upsale.toString().split(",").length;
    }

    private List<String> getUpsales(StringBuilder upsale) {
        return Arrays.asList(upsale.toString().split(","));
    }

    private List<Product> sortModel(String num, String name){
        List<Product> sortedProducts = new ArrayList<>();
        productList.forEach((p)-> {
            if(p.getSku().substring(4,5).equals(num)) {
                if(num.equals("4")) {
                    if(name.contains("Чёрный") || name.contains("Черный") || name.contains("Black") || (name.contains("Карбон") && !name.contains("Евродеталь"))) {
                        if(p.getName().contains("Чёрный") || p.getName().contains("Черный") || p.getName().contains("Black") || (p.getName().contains("Карбон") && !p.getName().contains("Евродеталь"))) {
                            sortedProducts.add(p);
                        }
                    } else if(name.contains("Белый")) {
                        if(p.getName().contains("Белый")) {
                            sortedProducts.add(p);
                        }
                    } else if(name.contains("Серый") || name.contains("Титан")) {
                        if(p.getName().contains("Серый") || p.getName().contains("Титан")) {
                            sortedProducts.add(p);
                        }
                    }
                } else if (num.equals("8")) {
                    if(name.contains("на крышу")) {
                        if(p.getName().contains("на крышу")) {
                            sortedProducts.add(p);
                        }
                    } else if(name.contains("на заднюю дверь") || name.contains("на запасное колесо")) {
                        if(p.getName().contains("на заднюю дверь") || p.getName().contains("на запасное колесо")) {
                            sortedProducts.add(p);
                        }
                    } else if(name.contains("на фаркоп")) {
                        if(p.getName().contains("на фаркоп")) {
                            sortedProducts.add(p);
                        }
                    }
                } else {
                    sortedProducts.add(p);
                }
            }
        });
        return sortedProducts;
    }

    private List<String> overPrice (List<Product> products, Double currentPrice, String currentModel, int count){
        List<String> result = new ArrayList<>();
        products.forEach((p)-> {
            if (!currentModel.equals(p.getSku()) && p.getPrice() >= currentPrice) {
                result.add(p.getSku());
            }
        });
        if (products.size() == 0) return result;
        int n = 1;
        while (result.size() < count) {
            products.sort(Comparator.comparing(Product::getPrice));

            for (Product p : products) {
                if (p.getSku().equals(currentModel)) {
                    int indx;
                    indx = products.indexOf(p) - n;
                    if (indx >= 0) {
                        if (!Objects.equals(products.get(indx).getPrice(), p.getPrice())) {
                            result.add(products.get(indx).getSku());
                        }
                    }
                    break;
                }
            }
            n++;
            if (result.size() == products.size() - 1) break;
        }

        Set<String> set = new HashSet<>();
        set.addAll(result);
        if (result.size() != set.size()) {
            System.out.println("q3423");
        }

        return result;
    }
}
