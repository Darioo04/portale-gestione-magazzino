package it.supermercato.magazzino.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "TipologiaProdotto")
public class ProductType {

    @Id
    @Column(name = "codice_ean", nullable = false, length = 20)
    private String eanCode;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "marca", nullable = false, length = 100)
    private String brand;

    @Positive
    @Column(name = "prezzo", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Positive
    @Column(name = "soglia_scorta", nullable = true)
    private Integer stockThreshold;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
    private Category category;

    public ProductType() {
    }

    public ProductType(String eanCode, String name, String brand, BigDecimal price, Integer stockThreshold, Category category) {
        this.eanCode = eanCode;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stockThreshold = stockThreshold;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
