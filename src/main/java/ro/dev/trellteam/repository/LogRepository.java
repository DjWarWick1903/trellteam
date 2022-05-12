package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Log save(Log log);
}
