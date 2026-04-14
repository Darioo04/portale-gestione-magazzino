package it.supermercato.magazzino.dto;

public class RoleDTO {
    
    private Integer id;
    private String name;

    public RoleDTO() {
    }

    public RoleDTO(Integer id, String name) {
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
