package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.dev.trellteam.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Optional<Role> findById(Long id);
    Role save(Role role);
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("delete from ROLE where name=:name")
    void deleteByName(@Param("name") String name);
}
