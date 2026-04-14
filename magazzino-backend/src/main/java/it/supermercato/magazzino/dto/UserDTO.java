package it.supermercato.magazzino.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {

    private Integer id;
    private String username;

    // This annotation prevents the password from being returned in the output JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Integer personId;
    private Integer roleId;

    public UserDTO() {
    }

    public UserDTO(Integer id, String username, String password, Integer personId, Integer roleId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.personId = personId;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
