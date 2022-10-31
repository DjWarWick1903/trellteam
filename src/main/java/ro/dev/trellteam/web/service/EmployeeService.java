package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.*;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.web.dto.AccountDto;
import ro.dev.trellteam.web.dto.DepartmentDto;
import ro.dev.trellteam.web.dto.EmployeeDto;
import ro.dev.trellteam.web.mapper.AccountMapper;
import ro.dev.trellteam.web.mapper.DepartmentMapper;
import ro.dev.trellteam.web.mapper.EmployeeMapper;
import ro.dev.trellteam.web.repository.EmployeeRepository;
import ro.dev.trellteam.web.request.employee.AssignEmployeeRequest;
import ro.dev.trellteam.web.request.employee.CreateEmployeeRequest;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {
    private final TransactionalOperations transactionalOperations;
    private final EmployeeRepository employeeRepository;
    private final OrganisationService organisationService;
    private final DepartmentService departmentService;
    private final EmployeeMapper employeeMapper;
    private final AccountMapper accountMapper;
    private final DepartmentMapper departmentMapper;
    private final RoleService roleService;

    public DepartmentDto unassignEmployeeToDepartment(final AssignEmployeeRequest request) {
        log.debug("EmployeeService--unassignEmployeeToDepartment--request: {}", request);

        final Employee employee = findById(request.getIdEmployee());
        final Organisation organisation = organisationService.findById(request.getIdOrganisation());
        final Set<Department> departments = organisation.getDepartments();

        Department department = null;
        for(final Department dep : departments) {
            if(dep.getName().equals(request.getDepartmentName())) {
                department = dep;
                break;
            }
        }

        if(department != null) {
            return departmentMapper.domainToDto(transactionalOperations.unassignEmployeeFromDepartment(employee, department));
        }
        throw new TrellGenericException("TRELL_ERR_9");
    }

    public DepartmentDto assignEmployeeToDepartment(final AssignEmployeeRequest request) {
        log.debug("EmployeeService--assignEmployeeToDepartment--request: {}", request);
        final Employee employee = findById(request.getIdEmployee());
        final Organisation organisation = organisationService.findById(request.getIdOrganisation());
        final Set<Department> departments = organisation.getDepartments();

        Department department = null;
        for(final Department dep : departments) {
            if(dep.getName().equals(request.getDepartmentName())) {
                department = dep;
                break;
            }
        }

        if(department != null) {
            return departmentMapper.domainToDto(transactionalOperations.assignEmployeeToDepartment(employee, department));
        }
        throw new TrellGenericException("TRELL_ERR_9");
    }

    public AccountDto createEmployee(final CreateEmployeeRequest request) {
        log.debug("EmployeeService--createEmployee--request: {}", request);
        Account account = accountMapper.dtoToDomain(request.getAccount());

        final Role role = roleService.findById(request.getIdRole());
        account.addRole(role);

        Department department = departmentService.findById(request.getIdDepartment());

        account = transactionalOperations.createEmployee(account, account.getEmployee(), department);
        return accountMapper.domainToDto(account);
    }

    /**
     * Method used to return a list of employees of an organisation.
     * @param idOrg
     * @return List<Employee>
     */
    public Set<EmployeeDto> listOrganisationEmployees(final Long idOrg) {
        log.debug("EmployeeService--listOrganisationEmployees--idOrg: {}", idOrg);
        final Organisation organisation = organisationService.findById(idOrg);
        final Set<Employee> employees = organisation.getEmployees();

        return employees.stream()
                .map(e -> employeeMapper.domainToDto(e))
                .collect(Collectors.toSet());
    }

    /**
     * Method used to return the employee starting from a provided id.
     * @param id
     * @return Employee
     */
    public Employee findById(final Long id) {
        log.debug("EmployeeService--findById--IN");
        log.debug("EmployeeService--findById--id: {}", id);
        final Employee employee = employeeRepository.findById(id).get();
        log.debug("EmployeeService--findById--employee: {}", employee);
        log.debug("EmployeeService--findById--OUT");
        return employee;
    }

    /**
     * Method used to save an employee in the database.
     * @param employee
     * @return Employee
     */
    public Employee save(Employee employee) {
        log.debug("EmployeeService--save--IN");
        employee = employeeRepository.save(employee);
        log.debug("EmployeeService--save--employee: {}", employee);
        log.debug("EmployeeService--save--IN");
        return employee;
    }

    public Employee saveAndFlush(final Employee employee) {
        log.debug("EmployeeService--saveAndFlush--IN");
        return employeeRepository.saveAndFlush(employee);
    }

    /**
     * Method used to delete an employee from the database.
     * @param id
     */
    public void deleteById(final Long id) {
        log.debug("EmployeeService--deleteById--IN");
        log.debug("EmployeeService--deleteById--id: {}", id);
        employeeRepository.deleteById(id);
        log.debug("EmployeeService--deleteById--OUT");
    }
}
