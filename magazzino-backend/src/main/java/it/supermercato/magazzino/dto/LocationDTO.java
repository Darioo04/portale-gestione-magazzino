package it.supermercato.magazzino.dto;

public class LocationDTO {
    
    private String area;
    private Integer aisle;
    private Integer shelf;

    public LocationDTO() {
    }

    public LocationDTO(String area, Integer aisle, Integer shelf) {
        this.area = area;
        this.aisle = aisle;
        this.shelf = shelf;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAisle() {
        return aisle;
    }

    public void setAisle(Integer aisle) {
        this.aisle = aisle;
    }

    public Integer getShelf() {
        return shelf;
    }

    public void setShelf(Integer shelf) {
        this.shelf = shelf;
    }
}
