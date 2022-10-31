package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.domain.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Log save(Log log);
}
