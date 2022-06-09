package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.Department;
import ro.dev.trellteam.model.Organisation;
import ro.dev.trellteam.repository.DepartmentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    /**
     * Method used to return the department starting from a provided name.
     * @param name
     * @return Department
     */
    public Department findByName(final String name) {
        log.debug("DepartmentService--findByName--IN");
        log.debug("DepartmentService--findByName--name: {}" , name);
        final Department department = departmentRepository.findByName(name);
        log.debug("DepartmentService--findByName--department: {}" , department.toString());
        log.debug("DepartmentService--findByName--OUT");
        return department;
    }

    /**
     * Method used to find a department based on the id of an employee.
     * @param id
     * @return Department
     */
    public List<Department> findByEmployeeId(final Long id) {
        log.debug("DepartmentService--findByEmployeeId--IN");
        log.debug("DepartmentService--findByEmployeeId--id: {}" , id);
        final List<Department> departments = departmentRepository.findByEmployeeId(id);
        log.debug("DepartmentService--findByEmployeeId--departments: {}" , departments);
        log.debug("DepartmentService--findByEmployeeId--OUT");
        return departments;
    }

    /**
     * Method used to return the department starting from a provided id.
     * @param id
     * @return Department
     */
    public Department findById(final Long id) {
        log.debug("DepartmentService--findById--IN");
        log.debug("DepartmentService--findById--id: {}" , id);
        final Department department = departmentRepository.findById(id).get();
        log.debug("DepartmentService--findById--department: {}" , department);
        log.debug("DepartmentService--findById--OUT");

        return department;
    }

    /**
     * Method used to save an department in the database.
     * @param department
     * @return Department
     */
    public Department save(Department department) {
        log.debug("DepartmentService--save--IN");
        department = departmentRepository.save(department);
        log.debug("DepartmentService--save--department: {}", department);
        log.debug("DepartmentService--save--OUT");
        return department;
    }

    /**
     * Method used to delete an department from the database.
     * @param id
     */
    public void deleteById(final Long id) {
        log.debug("DepartmentService--deleteById--IN");
        log.debug("DepartmentService--deleteById--id: {}" , id);
        departmentRepository.deleteById(id);
        log.debug("DepartmentService--deleteById--OUT");
    }
}
