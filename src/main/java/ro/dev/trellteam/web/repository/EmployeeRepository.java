package ro.dev.trellteam.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.dev.trellteam.domain.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //@Query("SELECT DISTINCT d.employees FROM DEPARTMENT d, ORGANISATION o, IN(o.departments) de WHERE o.id = :id")
    //List<Employee> listOrganisationEmployees(@Param("id") Long idOrg);

    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
}
