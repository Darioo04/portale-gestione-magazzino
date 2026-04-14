package it.supermercato.magazzino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
}
