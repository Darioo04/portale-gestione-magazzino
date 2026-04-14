package it.supermercato.magazzino.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Prodotto")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Positive
    @Column(name = "quantita", nullable = false)
    private Integer quantity;

    @Column(name = "data_scadenza", nullable = true)
    private LocalDateTime expirationDate;

    @Column(name = "lotto", nullable = false)
    private String batch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codice_ean", referencedColumnName = "codice_ean", nullable = false)
    private ProductType productType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "locazione_area", referencedColumnName = "area"),
        @JoinColumn(name = "locazione_corsia", referencedColumnName = "corsia"),
        @JoinColumn(name = "locazione_scaffale", referencedColumnName = "scaffale")
    })
    private Location location;

    public Product() {
    }

    public Product(Integer quantity, LocalDateTime expirationDate, String batch, ProductType productType, Location location) {
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.batch = batch;
        this.productType = productType;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
