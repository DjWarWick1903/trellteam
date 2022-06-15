package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.dev.trellteam.model.CardLog;

import java.util.Optional;

public interface CardLogRepository extends JpaRepository<CardLog, Long> {
    CardLog save(CardLog log);
    Optional<CardLog> findById(Long id);
}
