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
import ro.dev.trellteam.domain.Type;
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.web.service.TransactionalOperations;
import ro.dev.trellteam.web.service.TypeService;
import ro.dev.trellteam.web.service.AccountService;
import ro.dev.trellteam.web.service.DepartmentService;
import ro.dev.trellteam.web.service.OrganisationService;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/organisation", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrganisationController {
    private final AccountService accountService;
    private final DepartmentService departmentService;
    private final OrganisationService organisationService;
    private final TypeService typeService;
    private final TransactionalOperations transactionalOperations;


    @GetMapping("/main/{username}")
    public ResponseEntity<Organisation> getUserOrganisation(@PathVariable String username) {
        log.debug("OrganisationController--getUserOrganisation--IN");
        log.debug("OrganisationController--getUserOrganisation--username: {}", username);

        final Account account = accountService.getAccount(username);
        final List<Department> departments = departmentService.findByEmployeeId(account.getEmployee().getId());
        final Organisation organisation = organisationService.findByDepartmentId(departments.get(0).getId());

        log.debug("OrganisationController--getUserOrganisation--organisation: {}", organisation);
        log.debug("OrganisationController--getUserOrganisation--OUT");
        return ResponseEntity.ok().body(organisation);
    }

    @PostMapping("/main")
    public ResponseEntity<?> registerOrganisation(@RequestBody Map<String, Object> payload) {
        log.debug("OrganisationController--registerOrganisation--IN");
        log.debug("OrganisationController--registerOrganisation--payload: {}", payload.toString());

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/organisation/main").toUriString());
        log.debug("OrganisationController--registerOrganisation--uri: {}", uri);

        try {
            final Map<String, String> organisationData = (Map) payload.get("organisation");
            final String departmentName = (String) payload.get("depName");
            final Map<String, String> employeeData = (Map) payload.get("employee");
            final Map<String, String> accountData = (Map) payload.get("account");

            Organisation organisation = GeneralHelper.getOrganisationFromMap(organisationData);
            Employee employee = GeneralHelper.getEmployeeFromMap(employeeData);
            Account account = GeneralHelper.getAccountFromMap(accountData);
            Department department = new Department(null, departmentName, null, null);

            transactionalOperations.createOrganisationRepository(organisation, department, employee, account);
        } catch(final Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        final String message = "Organisation repository created succesfully.";
        return ResponseEntity.created(uri).body(message);
    }

    @PostMapping("/type")
    public ResponseEntity<Type> createType(@RequestBody Type type) {
        log.debug("OrganisationController--createType--IN");
        log.debug("OrganisationController--createType--idOrganisation: {}", type.getIdOrganisation());
        log.debug("OrganisationController--createType--name: {}", type.getName());

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/organisation/type").toUriString());
        log.debug("OrganisationController--createType--uri: {}", uri);

        type = typeService.save(type);

        log.debug("OrganisationController--createType--OUT");

        return ResponseEntity.created(uri).body(type);
    }

    @GetMapping("/type/{idOrg}")
    public ResponseEntity<List<Type>> getOrganisationTypes(@PathVariable Long idOrg) {
        log.debug("OrganisationController--getOrganisationTypes--IN");
        log.debug("OrganisationController--getOrganisationTypes--idOrganisation: {}", idOrg);

        final List<Type> types = typeService.listTypesByOrganisation(idOrg);

        log.debug("OrganisationController--getOrganisationTypes--OUT");

        return ResponseEntity.ok().body(types);
    }
}
