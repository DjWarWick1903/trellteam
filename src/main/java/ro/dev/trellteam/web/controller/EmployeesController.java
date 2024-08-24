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
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.web.dto.AccountDto;
import ro.dev.trellteam.web.dto.DepartmentDto;
import ro.dev.trellteam.web.dto.EmployeeDto;
import ro.dev.trellteam.web.request.employee.AssignEmployeeRequest;
import ro.dev.trellteam.web.request.employee.CreateEmployeeRequest;
import ro.dev.trellteam.web.response.ObjectResponse;
import ro.dev.trellteam.web.service.RoleService;
import ro.dev.trellteam.web.service.TransactionalOperations;
import ro.dev.trellteam.web.service.DepartmentService;
import ro.dev.trellteam.web.service.EmployeeService;
import ro.dev.trellteam.web.service.OrganisationService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employee/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class EmployeesController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @GetMapping("/organisation/{idOrg}")
    public ResponseEntity<ObjectResponse> getOrganisationEmployees(@PathVariable Long idOrg) {
        log.debug("EmployeesController--getOrganisationEmployees--IN");
        if(idOrg == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final Set<EmployeeDto> employees = employeeService.listOrganisationEmployees(idOrg);
        final ObjectResponse response = new ObjectResponse(employees);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/main")
    public ResponseEntity<ObjectResponse> createEmployee(@RequestBody @Valid CreateEmployeeRequest payload) {
        log.debug("EmployeesController--createEmployee--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/employee/v1/main").toUriString());

        final Department department = departmentService.findById(payload.getIdDepartment());
        final AccountDto account = employeeService.createEmployee(payload, department);
        final ObjectResponse response = new ObjectResponse(account);
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/department/assign")
    public ResponseEntity<ObjectResponse> assignEmployeeToDepartment(@RequestBody @Valid AssignEmployeeRequest payload) {
        log.debug("EmployeesController--assignEmployeeToDepartment--IN");
        final DepartmentDto department = employeeService.assignEmployeeToDepartment(payload);
        final ObjectResponse response = new ObjectResponse(department);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/department/unassign")
    public ResponseEntity<?> unassignEmployeeFromDepartment(@RequestBody @Valid AssignEmployeeRequest payload) {
        log.debug("EmployeesController--unassignEmployeeToDepartment--IN");
        final DepartmentDto department = employeeService.unassignEmployeeToDepartment(payload);
        final ObjectResponse response = new ObjectResponse(department);
        return ResponseEntity.ok().body(response);
    }
}
