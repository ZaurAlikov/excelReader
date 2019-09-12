package model;

public class Product {

    private String name;
    private String sku;
    private String model;
    private Double price;
    private String category;

    public Product(String name, String model, Double price) {
        this.name = name;
        this.sku = model;
        this.price = price;
    }

    public Product(String name, String sku, String model) {
        this.name = name;
        this.sku = sku;
        this.model = model;
    }

    public Product(String sku, String category, String name, Double price, String model) {
        this.sku = sku;
        this.category = category;
        this.name = name;
        this.model = model;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
