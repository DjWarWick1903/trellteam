package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.model.*;
import ro.dev.trellteam.service.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {
    private final TransactionalOperations transactionalOperations;
    private final AccountService accountService;
    private final DepartmentService departmentService;
    private final OrganisationService organisationService;
    private final EmployeeService employeeService;
    private final RoleService roleService;

    @GetMapping("/main/organisation/{username}")
    public ResponseEntity<?> getUserOrganisation(@PathVariable String username) {
        log.debug("UserController--getUserOrganisation--IN");
        log.debug("UserController--getUserOrganisation--username: {}", username);

        final Account account = accountService.getAccount(username);
        final List<Department> departments = departmentService.findByEmployeeId(account.getEmployee().getId());
        final Organisation organisation = organisationService.findByDepartmentId(departments.get(0).getId());

        log.debug("UserController--getUserOrganisation--organisation: {}", organisation);
        log.debug("UserController--getUserOrganisation--OUT");
        return ResponseEntity.ok().body(organisation);
    }

    @GetMapping("/main/organisation/employees/{idOrg}")
    public ResponseEntity<?> getOrganisationEmployees(@PathVariable Long idOrg) {
        log.debug("UserController--getOrganisationEmployees--IN");
        log.debug("UserController--getOrganisationEmployees--idOrganisation: {}", idOrg);

        final Set<Employee> employees = employeeService.listOrganisationEmployees(idOrg);

        log.debug("UserController--getOrganisationEmployees--organisation: {}", employees);
        log.debug("UserController--getOrganisationEmployees--OUT");
        return ResponseEntity.ok().body(employees);
    }

    @PostMapping("/main/organisation/department")
    public ResponseEntity<?> createDepartment(@RequestBody Map<String, String> payload) {
        log.debug("UserController--createDepartment--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/main/organisation/department").toUriString());
        log.debug("UserController--createDepartment--uri: {}", uri);

        final String departmentName = payload.get("depName");
        final Long idMan = Long.parseLong(payload.get("idMan"));
        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        log.debug("UserController--createDepartment--depName: {}", departmentName);
        log.debug("UserController--createDepartment--idMan: {}", idMan);
        log.debug("UserController--createDepartment--idOrg: {}", idOrg);

        Organisation organisation = organisationService.findById(idOrg);
        Department department = new Department();
        department.setName(departmentName);

        if(idMan != 0l) {
            final Employee manager = employeeService.findById(idMan);
            department.setManager(manager);
            department.addEmployee(manager);
        }

        department = transactionalOperations.createDepartment(organisation, department);

        log.debug("UserController--createDepartment--department: {}", department);
        log.debug("UserController--createDepartment--OUT");

        return ResponseEntity.created(uri).body(department);
    }

    @PostMapping("/main/organisation/department/delete")
    public ResponseEntity<?> deleteDepartment(@RequestBody Map<String, String> payload) {
        log.debug("UserController--deleteDepartment--IN");

        final String departmentName = payload.get("depName");
        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        boolean isDeleted = false;

        log.debug("UserController--deleteDepartment--depName: {}", departmentName);
        log.debug("UserController--deleteDepartment--idOrg: {}", idOrg);

        Organisation organisation = organisationService.findById(idOrg);
        Department department = departmentService.findByName(departmentName);

        transactionalOperations.removeDepartment(organisation, department);

        log.debug("UserController--deleteDepartment--OUT");
        return ResponseEntity.ok().body(organisation.getDepartments());
    }

    @PostMapping("/main/organisation/employee")
    public ResponseEntity<?> createEmployee(@RequestBody Map<String, Object> payload) {
        log.debug("UserController--createEmployee--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/main/organisation/department").toUriString());
        log.debug("UserController--createEmployee--uri: {}", uri);

        String response = null;

        try {
            final Map<String, String> accountMap = (Map<String, String>) payload.get("account");
            final Map<String, String> employeeMap = (Map<String, String>) payload.get("employee");

            Employee employee = SecurityHelper.getEmployeeFromMap(employeeMap);
            Account account = SecurityHelper.getAccountFromMap(accountMap);

            final Long roleId = Long.parseLong(accountMap.get("roleId"));
            final Long depId = Long.parseLong(employeeMap.get("depId"));

            final Role role = roleService.findById(roleId);
            account.addRole(role);
            Department department = departmentService.findById(depId);

            transactionalOperations.createEmployee(account, employee, department);

            response = "Employee created";
            log.debug("UserController--createEmployee--response: {}", response);
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        log.debug("UserController--createEmployee--OUT");
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/main/organisation/account/{username}")
    public ResponseEntity<?> fetchAccount(@PathVariable String username) {
        log.debug("UserController--fetchAccount--IN");
        log.debug("UserController--fetchAccount--username: {}", username);

        final Account account = accountService.getAccount(username);

        log.debug("UserController--fetchAccount--account: {}", account.toString());
        log.debug("UserController--fetchAccount--OUT");
        return ResponseEntity.ok().body(account);
    }

    @PostMapping("/main/organisation/department/employee/assign")
    public ResponseEntity<?> assignEmployeeToDepartment(@RequestBody Map<String, String> payload) {
        log.debug("UserController--assignEmployeeToDepartment--IN");

        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        final Long idEmp = Long.parseLong(payload.get("idEmp"));
        final String depName = payload.get("depName");

        log.debug("UserController--assignEmployeeToDepartment--idOrg: {}", idOrg);
        log.debug("UserController--assignEmployeeToDepartment--idEmp: {}", idEmp);
        log.debug("UserController--assignEmployeeToDepartment--depName: {}", depName);

        final Employee employee = employeeService.findById(idEmp);
        final Organisation organisation = organisationService.findById(idOrg);
        final List<Department> departments = organisation.getDepartments();

        Department department = null;
        for(final Department dep : departments) {
            if(dep.getName().equals(depName)) {
                department = dep;
                break;
            }
        }

        if(department != null) department = transactionalOperations.assignEmployeeToDepartment(employee, department);

        log.debug("UserController--assignEmployeeToDepartment--department: {}", department.toString());
        log.debug("UserController--assignEmployeeToDepartment--OUT");

        if(department != null) {
            return ResponseEntity.ok().body(department);
        } else {
            final String response = "The employee could not be assigned to the department.";
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/main/organisation/department/employee/unassign")
    public ResponseEntity<?> unassignEmployeeFromDepartment(@RequestBody Map<String, String> payload) {
        log.debug("UserController--unassignEmployeeToDepartment--IN");

        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        final Long idEmp = Long.parseLong(payload.get("idEmp"));
        final String depName = payload.get("depName");

        log.debug("UserController--unassignEmployeeToDepartment--idOrg: {}", idOrg);
        log.debug("UserController--unassignEmployeeToDepartment--idEmp: {}", idEmp);
        log.debug("UserController--unassignEmployeeToDepartment--depName: {}", depName);

        final Employee employee = employeeService.findById(idEmp);
        final Organisation organisation = organisationService.findById(idOrg);
        final List<Department> departments = organisation.getDepartments();

        Department department = null;
        for(final Department dep : departments) {
            if(dep.getName().equals(depName)) {
                department = dep;
                break;
            }
        }

        if(department != null) department = transactionalOperations.unassignEmployeeFromDepartment(employee, department);

        log.debug("UserController--unassignEmployeeToDepartment--department: {}", department.toString());
        log.debug("UserController--unassignEmployeeToDepartment--OUT");

        if(department != null) {
            return ResponseEntity.ok().body(department);
        } else {
            final String response = "The employee could not be unassigned from the department.";
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
