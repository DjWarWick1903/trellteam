package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Department;
import ro.dev.trellteam.domain.Employee;
import ro.dev.trellteam.domain.Organisation;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.DepartmentDto;
import ro.dev.trellteam.web.mapper.DepartmentMapper;
import ro.dev.trellteam.web.repository.DepartmentRepository;
import ro.dev.trellteam.web.request.department.CreateDepartmentRequest;
import ro.dev.trellteam.web.request.department.DeleteDepartmentRequest;
import ro.dev.trellteam.web.request.department.UpdateDepartmentRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final OrganisationService organisationService;
    private final TransactionalOperations transactionalOperations;
    private final EmployeeService employeeService;
    private final DepartmentMapper departmentMapper;

    public Set<DepartmentDto> getOrganisationDepartments(final Long idOrg) {
        log.debug("DepartmentService--getOrganisationDepartments--IN");
        final Organisation organisation = organisationService.findById(idOrg);
        final Set<Department> departments = organisation.getDepartments();

        log.debug("DepartmentService--getOrganisationDepartments--departments: {}", departments);

        return departments.stream()
                .map(e -> departmentMapper.domainToDto(e))
                .collect(Collectors.toSet());
    }

    public DepartmentDto createDepartment(final CreateDepartmentRequest request) {
        log.debug("DepartmentService--createDepartment--request: {}", request);

        Organisation organisation = organisationService.findById(request.getIdOrganisation());
        Department department = new Department();
        department.setName(request.getName());

        if(request.getIdManager() != null && request.getIdManager() != 0l) {
            final Employee manager = employeeService.findById(request.getIdManager());
            department.setManager(manager);
            department.addEmployee(manager);
        }

        department = transactionalOperations.createDepartment(organisation, department);
        return departmentMapper.domainToDto(department);
    }

    public Set<DepartmentDto> deleteDepartment(final DeleteDepartmentRequest request) {
        log.debug("DepartmentService--deleteDepartment--request: {}", request);
        boolean isDeleted = false;

        Organisation organisation = organisationService.findById(request.getIdOrganisation());
        Department department = findByName(request.getName());

        isDeleted = transactionalOperations.removeDepartment(organisation, department);

        if(isDeleted) {
            return organisation.getDepartments().stream()
                    .map(e -> departmentMapper.domainToDto(e))
                    .collect(Collectors.toSet());
        } else {
            throw new TrellGenericException("TRELL_ERR_7");
        }
    }

    public DepartmentDto updateDepartment(final UpdateDepartmentRequest request) {
        log.debug("DepartmentService--updateDepartment--request: {}", request);
        Department department = findById(request.getIdDepartment());
        department.setName(request.getName());

        if(request.getIdManager() != null) {
            final Employee employee = request.getIdManager() == 0 ? null : employeeService.findById(request.getIdManager());
            department.setManager(employee);
        }

        return departmentMapper.domainToDto(save(department));
    }

    /**
     * Method used to return the department starting from a provided name.
     * @param name
     * @return Department
     */
    public Department findByName(final String name) {
        log.debug("DepartmentService--findByName--IN");
        log.debug("DepartmentService--findByName--name: {}" , name);

        Department department = null;
        try {
            department = departmentRepository.findByName(name);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_5");
        }

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

        List<Department> departments = null;
        try {
            departments = departmentRepository.findByEmployeeId(id);
        } catch(final Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_5");
        }

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

        Department department = null;
        try {
            department = departmentRepository.findById(id).get();
        } catch(final Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_5");
        }
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
