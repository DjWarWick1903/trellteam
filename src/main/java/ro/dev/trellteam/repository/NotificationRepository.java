package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAccount(Long idAccount);
    Notification save(Notification account);
}
