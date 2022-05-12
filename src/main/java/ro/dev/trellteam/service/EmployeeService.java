package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.Department;
import ro.dev.trellteam.model.Employee;
import ro.dev.trellteam.repository.DepartmentRepository;
import ro.dev.trellteam.repository.EmployeeRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    /**
     * Method used to return the employee starting from a provided id.
     * @param id
     * @return Employee
     */
    public Employee findById(final Long id) {
        log.debug("EmployeeService--Fetching department with id {}" , id);
        return employeeRepository.findById(id).get();
    }

    /**
     * Method used to save an employee in the database.
     * @param department
     * @return Employee
     */
    public Employee save(final Employee employee) {
        log.debug("EmployeeService--Saving department");
        return employeeRepository.save(employee);
    }

    public Employee saveAndFlush(final Employee employee) {
        log.debug("EmployeeService--Saving department");
        return employeeRepository.saveAndFlush(employee);
    }

    /**
     * Method used to delete an employee from the database.
     * @param id
     */
    public void deleteById(final Long id) {
        log.debug("EmployeeService--Deleting department with id {}", id);
        employeeRepository.deleteById(id);
    }
}
