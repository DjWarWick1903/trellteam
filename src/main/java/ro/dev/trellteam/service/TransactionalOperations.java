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
}
