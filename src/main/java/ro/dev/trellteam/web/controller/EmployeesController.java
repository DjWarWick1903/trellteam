package ro.dev.trellteam.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Department;
import ro.dev.trellteam.domain.Employee;
import ro.dev.trellteam.domain.Organisation;
import ro.dev.trellteam.domain.Role;
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.web.service.RoleService;
import ro.dev.trellteam.web.service.TransactionalOperations;
import ro.dev.trellteam.web.service.DepartmentService;
import ro.dev.trellteam.web.service.EmployeeService;
import ro.dev.trellteam.web.service.OrganisationService;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employee", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class EmployeesController {
    private final TransactionalOperations transactionalOperations;
    private final DepartmentService departmentService;
    private final OrganisationService organisationService;
    private final EmployeeService employeeService;
    private final RoleService roleService;

    @GetMapping("/organisation/{idOrg}")
    public ResponseEntity<Set<Employee>> getOrganisationEmployees(@PathVariable Long idOrg) {
        log.debug("EmployeesController--getOrganisationEmployees--IN");
        log.debug("EmployeesController--getOrganisationEmployees--idOrganisation: {}", idOrg);

        final Set<Employee> employees = employeeService.listOrganisationEmployees(idOrg);

        log.debug("EmployeesController--getOrganisationEmployees--organisation: {}", employees);
        log.debug("EmployeesController--getOrganisationEmployees--OUT");
        return ResponseEntity.ok().body(employees);
    }

    @PostMapping("/main")
    public ResponseEntity<?> createEmployee(@RequestBody Map<String, Object> payload) {
        log.debug("EmployeesController--createEmployee--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/main/organisation/department").toUriString());
        log.debug("EmployeesController--createEmployee--uri: {}", uri);

        String response = null;

        try {
            final Map<String, String> accountMap = (Map<String, String>) payload.get("account");
            final Map<String, String> employeeMap = (Map<String, String>) payload.get("employee");

            Employee employee = GeneralHelper.getEmployeeFromMap(employeeMap);
            Account account = GeneralHelper.getAccountFromMap(accountMap);

            final Long roleId = Long.parseLong(accountMap.get("roleId"));
            final Long depId = Long.parseLong(employeeMap.get("depId"));

            final Role role = roleService.findById(roleId);
            account.addRole(role);
            Department department = departmentService.findById(depId);

            transactionalOperations.createEmployee(account, employee, department);

            response = "Employee created";
            log.debug("EmployeesController--createEmployee--response: {}", response);
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        log.debug("EmployeesController--createEmployee--OUT");
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/department/assign")
    public ResponseEntity<?> assignEmployeeToDepartment(@RequestBody Map<String, String> payload) {
        log.debug("EmployeesController--assignEmployeeToDepartment--IN");

        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        final Long idEmp = Long.parseLong(payload.get("idEmp"));
        final String depName = payload.get("depName");

        log.debug("EmployeesController--assignEmployeeToDepartment--idOrg: {}", idOrg);
        log.debug("EmployeesController--assignEmployeeToDepartment--idEmp: {}", idEmp);
        log.debug("EmployeesController--assignEmployeeToDepartment--depName: {}", depName);

        final Employee employee = employeeService.findById(idEmp);
        final Organisation organisation = organisationService.findById(idOrg);
        final Set<Department> departments = organisation.getDepartments();

        Department department = null;
        for(final Department dep : departments) {
            if(dep.getName().equals(depName)) {
                department = dep;
                break;
            }
        }

        if(department != null) department = transactionalOperations.assignEmployeeToDepartment(employee, department);

        log.debug("EmployeesController--assignEmployeeToDepartment--department: {}", department.toString());
        log.debug("EmployeesController--assignEmployeeToDepartment--OUT");

        if(department != null) {
            return ResponseEntity.ok().body(department);
        } else {
            final String response = "The employee could not be assigned to the department.";
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/department/unassign")
    public ResponseEntity<?> unassignEmployeeFromDepartment(@RequestBody Map<String, String> payload) {
        log.debug("EmployeesController--unassignEmployeeToDepartment--IN");

        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        final Long idEmp = Long.parseLong(payload.get("idEmp"));
        final String depName = payload.get("depName");

        log.debug("EmployeesController--unassignEmployeeToDepartment--idOrg: {}", idOrg);
        log.debug("EmployeesController--unassignEmployeeToDepartment--idEmp: {}", idEmp);
        log.debug("EmployeesController--unassignEmployeeToDepartment--depName: {}", depName);

        final Employee employee = employeeService.findById(idEmp);
        final Organisation organisation = organisationService.findById(idOrg);
        final Set<Department> departments = organisation.getDepartments();

        Department department = null;
        for(final Department dep : departments) {
            if(dep.getName().equals(depName)) {
                department = dep;
                break;
            }
        }

        if(department != null) department = transactionalOperations.unassignEmployeeFromDepartment(employee, department);

        log.debug("EmployeesController--unassignEmployeeToDepartment--department: {}", department.toString());
        log.debug("EmployeesController--unassignEmployeeToDepartment--OUT");

        if(department != null) {
            return ResponseEntity.ok().body(department);
        } else {
            final String response = "The employee could not be unassigned from the department.";
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
