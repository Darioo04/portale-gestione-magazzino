package it.supermercato.magazzino.dto;

import java.time.LocalDateTime;
import it.supermercato.magazzino.entity.NotificationPriority;

public class NotificationDTO {

    private Integer id;
    private String message;
    private LocalDateTime creationDate;
    private Boolean read;
    private NotificationPriority priority;
    private Integer userId;

    public NotificationDTO() {
    }

    public NotificationDTO(Integer id, String message, LocalDateTime creationDate, Boolean read, NotificationPriority priority, Integer userId) {
        this.id = id;
        this.message = message;
        this.creationDate = creationDate;
        this.read = read;
        this.priority = priority;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
