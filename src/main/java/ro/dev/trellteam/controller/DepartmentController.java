package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.model.Department;
import ro.dev.trellteam.model.Employee;
import ro.dev.trellteam.model.Organisation;
import ro.dev.trellteam.service.*;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/department", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DepartmentController {
    private final TransactionalOperations transactionalOperations;
    private final DepartmentService departmentService;
    private final OrganisationService organisationService;
    private final EmployeeService employeeService;

    @GetMapping("/main/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        log.debug("DepartmentController--getDepartment--IN");

        final Department department = departmentService.findById(id);

        log.debug("DepartmentController--getDepartment--department: {}", department.toString());
        log.debug("DepartmentController--getDepartment--OUT");

        return ResponseEntity.ok().body(department);
    }

    @GetMapping("/organisation/{idOrg}")
    public ResponseEntity<Set<Department>> getOrganisationDepartments(@PathVariable Long idOrg) {
        log.debug("DepartmentController--getDepartment--IN");

        final Organisation organisation = organisationService.findById(idOrg);
        final Set<Department> departments = organisation.getDepartments();

        log.debug("DepartmentController--getDepartment--department: {}", departments.toString());
        log.debug("DepartmentController--getDepartment--OUT");

        return ResponseEntity.ok().body(departments);
    }

    @PostMapping("/main")
    public ResponseEntity<?> createDepartment(@RequestBody Map<String, String> payload) {
        log.debug("DepartmentController--createDepartment--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/main/organisation/department").toUriString());
        log.debug("DepartmentController--createDepartment--uri: {}", uri);

        final String departmentName = payload.get("depName");
        final Long idMan = Long.parseLong(payload.get("idMan"));
        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        log.debug("DepartmentController--createDepartment--depName: {}", departmentName);
        log.debug("DepartmentController--createDepartment--idMan: {}", idMan);
        log.debug("DepartmentController--createDepartment--idOrg: {}", idOrg);

        Organisation organisation = organisationService.findById(idOrg);
        Department department = new Department();
        department.setName(departmentName);

        if(idMan != null && idMan != 0l) {
            final Employee manager = employeeService.findById(idMan);
            department.setManager(manager);
            department.addEmployee(manager);
        }

        department = transactionalOperations.createDepartment(organisation, department);

        log.debug("DepartmentController--createDepartment--department: {}", department);
        log.debug("DepartmentController--createDepartment--OUT");

        return ResponseEntity.created(uri).body(department);
    }

    @DeleteMapping("/main")
    public ResponseEntity<?> deleteDepartment(@RequestBody Map<String, String> payload) {
        log.debug("DepartmentController--deleteDepartment--IN");

        final String departmentName = payload.get("depName");
        final Long idOrg = Long.parseLong(payload.get("idOrg"));
        boolean isDeleted = false;

        log.debug("DepartmentController--deleteDepartment--depName: {}", departmentName);
        log.debug("DepartmentController--deleteDepartment--idOrg: {}", idOrg);

        Organisation organisation = organisationService.findById(idOrg);
        Department department = departmentService.findByName(departmentName);

        transactionalOperations.removeDepartment(organisation, department);

        log.debug("DepartmentController--deleteDepartment--OUT");
        return ResponseEntity.ok().body(organisation.getDepartments());
    }

    @PutMapping("/main")
    public ResponseEntity<Department> updateDepartment(@RequestBody Map<String, String> payload) {
        log.debug("DepartmentController--updateDepartment--IN");

        final Long idDep = Long.parseLong(payload.get("idDep"));
        final String depName = payload.get("depName");
        final Long managerId = Long.parseLong(payload.get("idMan"));

        log.debug("DepartmentController--updateDepartment--idDep: {}", idDep);
        log.debug("DepartmentController--updateDepartment--depName: {}", depName);
        log.debug("DepartmentController--updateDepartment--managerId: {}", managerId);

        Department department = departmentService.findById(idDep);
        department.setName(depName);

        if(managerId != null) {
            final Employee employee = managerId == 0 ? null : employeeService.findById(managerId);
            department.setManager(employee);
        }

        department = departmentService.save(department);

        log.debug("DepartmentController--updateDepartment--department: {}", department.toString());
        log.debug("DepartmentController--updateDepartment--OUT");

        return ResponseEntity.ok().body(department);
    }
}
