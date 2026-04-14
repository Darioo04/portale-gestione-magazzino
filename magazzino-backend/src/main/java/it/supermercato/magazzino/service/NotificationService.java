package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.NotificationDTO;

public interface NotificationService {

    List<NotificationDTO> getAllNotifications();

    NotificationDTO getNotificationById(Integer id);

    NotificationDTO createNotification(NotificationDTO notificationDTO);

    NotificationDTO updateNotification(Integer id, NotificationDTO notificationDTO);

    void deleteNotification(Integer id);
}
