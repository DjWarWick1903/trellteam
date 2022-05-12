package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Organisation;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    Organisation findByName(String name);
    Optional<Organisation> findById(Long id);

    Organisation save(Organisation organisation);

    void deleteById(Long id);
}
