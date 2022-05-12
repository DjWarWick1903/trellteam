package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.Department;
import ro.dev.trellteam.model.Organisation;
import ro.dev.trellteam.repository.DepartmentRepository;

import javax.transaction.Transactional;

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
        log.debug("DepartmentService--Fetching department with name {}" , name);
        return departmentRepository.findByName(name);
    }

    /**
     * Method used to return the department starting from a provided id.
     * @param id
     * @return Department
     */
    public Department findById(final Long id) {
        log.debug("DepartmentService--Fetching department with id {}" , id);
        return departmentRepository.findById(id).get();
    }

    /**
     * Method used to save an department in the database.
     * @param department
     * @return Department
     */
    public Department save(final Department department) {
        log.debug("DepartmentService--Saving department");
        return  departmentRepository.save(department);
    }

    public Department saveAndFlush(final Department department) {
        log.debug("DepartmentService--Saving department");
        return  departmentRepository.saveAndFlush(department);
    }

    /**
     * Method used to delete an department from the database.
     * @param id
     */
    public void deleteById(final Long id) {
        log.debug("DepartmentService--Deleting department with id {}", id);
        departmentRepository.deleteById(id);
    }
}
