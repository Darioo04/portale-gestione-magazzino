package it.supermercato.magazzino.dto;


// Data Transfer Object (DTO) for Category entity, used to transfer data between the backend and frontend layers without exposing the internal structure of the entity
public class CategoryDTO {
    
    private Integer id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /** 
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    /** 
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
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
