package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findById(Long id);
    Department findByName(String name);

    @Query("SELECT d FROM DEPARTMENT d, IN(d.employees) e WHERE e.id = :id")
    Department findByEmployeeId(@Param("id") Long id);

    Department save(Department department);
    void deleteById(Long id);
}
