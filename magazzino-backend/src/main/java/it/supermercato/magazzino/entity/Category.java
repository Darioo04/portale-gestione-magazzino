package it.supermercato.magazzino.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Categoria")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id; // Wrapper class Integer is used instead of primitive int to allow for null values before the entity is persisted and gets an ID assigned by the database

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String name;

    // JPA empty constructor
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    /** 
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    /** 
     * @return String
     */
    public String getName() {
        return name;
    }

    /** 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
