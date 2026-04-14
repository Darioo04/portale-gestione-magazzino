package it.supermercato.magazzino.dto;

import java.math.BigDecimal;

public class ProductTypeDTO {

    private String eanCode;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stockThreshold;
    private Integer categoryId;

    public ProductTypeDTO() {
    }

    public ProductTypeDTO(String eanCode, String name, String brand, BigDecimal price, Integer stockThreshold,
            Integer categoryId) {
        this.eanCode = eanCode;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stockThreshold = stockThreshold;
        this.categoryId = categoryId;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockThreshold() {
        return stockThreshold;
    }

    public void setStockThreshold(Integer stockThreshold) {
        this.stockThreshold = stockThreshold;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
