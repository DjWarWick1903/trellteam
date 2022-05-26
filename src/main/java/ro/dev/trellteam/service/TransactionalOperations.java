package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.dev.trellteam.model.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionalOperations {

    private final OrganisationService organisationService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final RoleService roleService;

    @Transactional
    public void createOrganisationRepository(Organisation organisation, Department department, Employee employee, Account account) {
        log.debug("TransactionalOperations--createOrganisationRepository--IN");
        //employee = employeeService.saveAndFlush(employee);

        final Role role = roleService.findByName("ADMIN");
        account.addRole(role);
        account.setEmployee(employee);
        account = accountService.save(account);

        department.addEmployee(employee);

        organisation.addDepartment(department);
        organisation = organisationService.save(organisation);

        log.debug("TransactionalOperations--createOrganisationRepository--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--department: {}", department.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--employee: {}", employee.toString());
        log.debug("TransactionalOperations--createOrganisationRepository--account: {}", account.toString());

        log.debug("TransactionalOperations--createOrganisationRepository--OUT");
    }

    @Transactional
    public void createEmployee(Account account, Employee employee, Department department) {
        log.debug("TransactionalOperations--createEmployee--IN");

        employee = employeeService.save(employee);

        account.setEmployee(employee);
        department.addEmployee(employee);

        account = accountService.save(account);
        department = departmentService.save(department);

        log.debug("TransactionalOperations--createEmployee--employee: {}", employee.toString());
        log.debug("TransactionalOperations--createEmployee--account: {}", account.toString());
        log.debug("TransactionalOperations--createEmployee--department: {}", department.toString());
        log.debug("TransactionalOperations--createEmployee--OUT");
    }

    @Transactional
    public Department createDepartment(Organisation organisation, Department department) {
        log.debug("TransactionalOperations--removeDepartment--IN");
        log.debug("TransactionalOperations--removeDepartment--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--removeDepartment--department: {}", department.toString());

        boolean isRemoved = false;
        try {
            department = departmentService.save(department);
            organisation.addDepartment(department);
            organisationService.save(organisation);
            isRemoved = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--removeDepartment--isRemoved: {}", isRemoved);
        log.debug("TransactionalOperations--removeDepartment--OUT");
        return isRemoved == true ? department : null;
    }

    @Transactional
    public boolean removeDepartment(Organisation organisation, Department department) {
        log.debug("TransactionalOperations--removeDepartment--IN");
        log.debug("TransactionalOperations--removeDepartment--organisation: {}", organisation.toString());
        log.debug("TransactionalOperations--removeDepartment--department: {}", department.toString());

        boolean isRemoved = false;
        try {
            organisation.removeDepartment(department);
            department.purgeEmployees();
            departmentService.deleteById(department.getId());
            organisationService.save(organisation);
            isRemoved = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--removeDepartment--isRemoved: {}", isRemoved);
        log.debug("TransactionalOperations--removeDepartment--OUT");
        return isRemoved;
    }

    @Transactional
    public Department assignEmployeeToDepartment(Employee employee, Department department) {
        log.debug("TransactionalOperations--assignEmployeeToDepartment--IN");
        log.debug("TransactionalOperations--assignEmployeeToDepartment--employee: {}", employee.toString());
        log.debug("TransactionalOperations--assignEmployeeToDepartment--department: {}", department.toString());

        boolean isAssigned = false;
        try {
            department.addEmployee(employee);
            departmentService.save(department);
            isAssigned = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--assignEmployeeToDepartment--isAssigned: {}", isAssigned);
        log.debug("TransactionalOperations--assignEmployeeToDepartment--OUT");

        return isAssigned == true ? department : null;
    }

    @Transactional
    public Department unassignEmployeeFromDepartment(Employee employee, Department department) {
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--IN");
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--employee: {}", employee.toString());
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--department: {}", department.toString());

        boolean isUnassigned = false;
        try {
            department.removeEmployee(employee);
            departmentService.save(department);
            isUnassigned = true;
        } catch(final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--isUnassigned: {}", isUnassigned);
        log.debug("TransactionalOperations--unassignEmployeeFromDepartment--OUT");

        return isUnassigned == true ? department : null;
    }
}
