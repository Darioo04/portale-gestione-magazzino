package it.supermercato.magazzino.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Locazione")
public class Location {

    @EmbeddedId
    private LocationId id;

    @OneToOne(mappedBy = "location")
    private Product product;

    public Location() {
    }

    public Location(String area, Integer aisle, Integer shelf) {
        this.id = new LocationId();
        this.id.setArea(area);
        this.id.setAisle(aisle);
        this.id.setShelf(shelf);
    }

    public LocationId getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    

    @Embeddable
    public static class LocationId implements Serializable {

        @Column(name = "area", nullable = false, length = 50)
        private String area;

        @Positive
        @Column(name = "corsia", nullable = false)
        private Integer aisle;

        @Positive
        @Column(name = "scaffale", nullable = false)
        private Integer shelf;

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

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(this.area);
            hash = 53 * hash + Objects.hashCode(this.aisle);
            hash = 53 * hash + Objects.hashCode(this.shelf);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LocationId other = (LocationId) obj;
            if (!Objects.equals(this.area, other.area)) {
                return false;
            }
            if (!Objects.equals(this.aisle, other.aisle)) {
                return false;
            }
            return Objects.equals(this.shelf, other.shelf);
        }


    }
    
}
