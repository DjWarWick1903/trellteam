package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Department;
import ro.dev.trellteam.model.Employee;
import ro.dev.trellteam.model.Organisation;
import ro.dev.trellteam.service.AccountService;
import ro.dev.trellteam.service.DepartmentService;
import ro.dev.trellteam.service.EmployeeService;
import ro.dev.trellteam.service.OrganisationService;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {
    private final AccountService accountService;
    private final DepartmentService departmentService;
    private final OrganisationService organisationService;
    private final EmployeeService employeeService;

    @GetMapping("/main/organisation/{username}")
    public ResponseEntity<?> getUserOrganisation(@PathVariable String username) {
        log.debug("UserController--getUserOrganisation--IN");
        log.debug("UserController--getUserOrganisation--username: {}", username);

        final Account account = accountService.getAccount(username);
        final Department department = departmentService.findByEmployeeId(account.getEmployee().getId());
        final Organisation organisation = organisationService.findByDepartmentId(department.getId());

        log.debug("UserController--getUserOrganisation--organisation: {}", organisation);
        log.debug("UserController--getUserOrganisation--OUT");
        return ResponseEntity.ok().body(organisation);
    }

    @GetMapping("/main/organisation/employees/{idOrg}")
    public ResponseEntity<?> getOrganisationEmployees(@PathVariable Long idOrg) {
        log.debug("UserController--getOrganisationEmployees--IN");
        log.debug("UserController--getOrganisationEmployees--idOrganisation: {}", idOrg);

        final List<Employee> employees = employeeService.listOrganisationEmployees(idOrg);

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

        department = departmentService.save(department);
        log.debug("UserController--createDepartment--department: {}", department);
        organisation.addDepartment(department);

        organisation = organisationService.save(organisation);

        log.debug("UserController--createDepartment--OUT");
        return ResponseEntity.created(uri).body(department);
    }

}
