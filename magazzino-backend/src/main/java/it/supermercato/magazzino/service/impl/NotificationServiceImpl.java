package it.supermercato.magazzino.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.NotificationDTO;
import it.supermercato.magazzino.entity.Notification;
import it.supermercato.magazzino.entity.User;
import it.supermercato.magazzino.repository.NotificationRepository;
import it.supermercato.magazzino.repository.UserRepository;
import it.supermercato.magazzino.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public NotificationDTO getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("No notification found with ID: " + id));
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        validateInput(notificationDTO);

        User user = userRepository.findById(notificationDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Use current time if creationDate is not provided
        LocalDateTime creationDate = notificationDTO.getCreationDate() != null ? notificationDTO.getCreationDate() : LocalDateTime.now();
        // Default read to false if not provided
        Boolean read = notificationDTO.getRead() != null ? notificationDTO.getRead() : false;

        Notification newEntity = new Notification(notificationDTO.getMessage(), creationDate, read, notificationDTO.getPriority(), user);
        Notification savedEntity = notificationRepository.save(newEntity);

        return toDTO(savedEntity);
    }

    @Override
    public NotificationDTO updateNotification(Integer id, NotificationDTO notificationDTO) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No notification found with ID: " + id));

        if (notificationDTO.getMessage() != null && !notificationDTO.getMessage().isBlank()) {
            existingNotification.setMessage(notificationDTO.getMessage());
        }

        if (notificationDTO.getCreationDate() != null) {
            existingNotification.setCreationDate(notificationDTO.getCreationDate());
        }

        if (notificationDTO.getRead() != null) {
            existingNotification.setRead(notificationDTO.getRead());
        }

        if (notificationDTO.getPriority() != null) {
            existingNotification.setPriority(notificationDTO.getPriority());
        }

        if (notificationDTO.getUserId() != null) {
            User user = userRepository.findById(notificationDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existingNotification.setUser(user);
        }

        Notification savedEntity = notificationRepository.save(existingNotification);
        return toDTO(savedEntity);
    }

    @Override
    public void deleteNotification(Integer id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("No notification found with ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    private void validateInput(NotificationDTO dto) {
        if (dto == null) throw new IllegalArgumentException("NotificationDTO cannot be null");
        if (dto.getMessage() == null || dto.getMessage().isBlank()) throw new IllegalArgumentException("Message is required");
        if (dto.getPriority() == null) throw new IllegalArgumentException("Priority is required");
        if (dto.getUserId() == null) throw new IllegalArgumentException("User ID is required");
    }

    private NotificationDTO toDTO(Notification entity) {
        return new NotificationDTO(
                entity.getId(),
                entity.getMessage(),
                entity.getCreationDate(),
                entity.getRead(),
                entity.getPriority(),
                entity.getUser().getId()
        );
    }
}
