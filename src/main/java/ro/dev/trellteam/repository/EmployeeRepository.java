package ro.dev.trellteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //@Query("SELECT DISTINCT d.employees FROM DEPARTMENT d, ORGANISATION o, IN(o.departments) de WHERE o.id = :id")
    //List<Employee> listOrganisationEmployees(@Param("id") Long idOrg);

    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
}
