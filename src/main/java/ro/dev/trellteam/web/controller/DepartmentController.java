package ro.dev.trellteam.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.domain.Department;
import ro.dev.trellteam.domain.Employee;
import ro.dev.trellteam.domain.Organisation;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.DepartmentDto;
import ro.dev.trellteam.web.mapper.DepartmentMapper;
import ro.dev.trellteam.web.request.department.CreateDepartmentRequest;
import ro.dev.trellteam.web.request.department.DeleteDepartmentRequest;
import ro.dev.trellteam.web.request.department.UpdateDepartmentRequest;
import ro.dev.trellteam.web.response.ObjectResponse;
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
@RequestMapping(value = "/department/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentService;

    private final DepartmentMapper departmentMapper;

    @GetMapping("/main/{id}")
    public ResponseEntity<ObjectResponse> getDepartmentById(@PathVariable Long id) {
        log.debug("DepartmentController--getDepartmentById--IN");
        if(id == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }
        final Department department = departmentService.findById(id);
        final ObjectResponse response = new ObjectResponse(departmentMapper.domainToDto(department));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/organisation/{idOrg}")
    public ResponseEntity<ObjectResponse> getOrganisationDepartments(@PathVariable Long idOrg) {
        log.debug("DepartmentController--getOrganisationDepartments--IN");
        if(idOrg == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }
        final ObjectResponse response = new ObjectResponse(departmentService.getOrganisationDepartments(idOrg));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/main")
    public ResponseEntity<ObjectResponse> createDepartment(@RequestBody @Valid CreateDepartmentRequest payload) {
        log.debug("DepartmentController--createDepartment--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/department/v1/main").toUriString());
        log.debug("DepartmentController--createDepartment--uri: {}", uri);

        final ObjectResponse response = new ObjectResponse(departmentService.createDepartment(payload));
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/main")
    public ResponseEntity<ObjectResponse> deleteDepartment(@RequestBody @Valid DeleteDepartmentRequest payload) {
        log.debug("DepartmentController--deleteDepartment--IN");
        final ObjectResponse response = new ObjectResponse(departmentService.deleteDepartment(payload));
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/main")
    public ResponseEntity<ObjectResponse> updateDepartment(@RequestBody @Valid UpdateDepartmentRequest payload) {
        log.debug("DepartmentController--updateDepartment--IN");
        final ObjectResponse response = new ObjectResponse(departmentService.updateDepartment(payload));
        return ResponseEntity.ok().body(response);
    }
}
