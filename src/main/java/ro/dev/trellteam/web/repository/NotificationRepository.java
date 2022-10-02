package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAccount(Long idAccount);
    Notification save(Notification account);
}
