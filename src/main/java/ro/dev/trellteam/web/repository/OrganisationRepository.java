package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.domain.Organisation;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    Organisation findByName(String name);
    @Query("SELECT o FROM ORGANISATION o, IN(o.departments) d WHERE d.id = :id")
    Organisation findByDepartmentId(@Param("id") Long id);
    Optional<Organisation> findById(Long id);

    Organisation save(Organisation organisation);

    void deleteById(Long id);
}
