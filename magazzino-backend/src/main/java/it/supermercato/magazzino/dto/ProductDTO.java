package it.supermercato.magazzino.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDTO {
    
    private Integer id;
    private String eanCode;
    private Integer quantity;
    private LocalDateTime expirationDate;
    private String batch;

    // Read-only fields populated from ProductType via the EAN code
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String brand;

    // Location composite key fields
    private String locationArea;
    private Integer locationAisle;
    private Integer locationShelf;

    public ProductDTO() {
    }

    public ProductDTO(Integer id, String eanCode, Integer quantity, LocalDateTime expirationDate, String batch,
                      String productName, String brand,
                      String locationArea, Integer locationAisle, Integer locationShelf) {
        this.id = id;
        this.eanCode = eanCode;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.batch = batch;
        this.productName = productName;
        this.brand = brand;
        this.locationArea = locationArea;
        this.locationAisle = locationAisle;
        this.locationShelf = locationShelf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }

    public Integer getLocationAisle() {
        return locationAisle;
    }

    public void setLocationAisle(Integer locationAisle) {
        this.locationAisle = locationAisle;
    }

    public Integer getLocationShelf() {
        return locationShelf;
    }

    public void setLocationShelf(Integer locationShelf) {
        this.locationShelf = locationShelf;
    }
}
