package it.supermercato.magazzino.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovementDTO {

    private Integer id;
    private LocalDateTime movementDate;
    private Integer quantity;
    private Integer productId;
    private Integer userId;

    // Additional read-only fields for details
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productBrand;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productEanCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userFirstName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userLastName;

    public MovementDTO() {
    }

    public MovementDTO(Integer id, LocalDateTime movementDate, Integer quantity, Integer productId, Integer userId,
                       String productName, String productBrand, String productEanCode,
                       String username, String userFirstName, String userLastName) {
        this.id = id;
        this.movementDate = movementDate;
        this.quantity = quantity;
        this.productId = productId;
        this.userId = userId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productEanCode = productEanCode;
        this.username = username;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductEanCode() {
        return productEanCode;
    }

    public void setProductEanCode(String productEanCode) {
        this.productEanCode = productEanCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
