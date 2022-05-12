package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findById(Long id);
    Department findByName(String name);
    Department save(Department department);
    void deleteById(Long id);
}
