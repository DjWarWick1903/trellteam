package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Department;
import ro.dev.trellteam.model.Organisation;
import ro.dev.trellteam.service.AccountService;
import ro.dev.trellteam.service.DepartmentService;
import ro.dev.trellteam.service.OrganisationService;

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

    @GetMapping("/main/organisation/{username}")
    public ResponseEntity<?> getMainPageDetails(@PathVariable String username) {
        log.debug("UserController--getMainPageDetails--IN");
        log.debug("UserController--getMainPageDetails--username: {}", username);

        final Account account = accountService.getAccount(username);
        final Department department = departmentService.findByEmployeeId(account.getEmployee().getId());
        final Organisation organisation = organisationService.findByDepartmentId(department.getId());
        log.debug("UserController--getMainPageDetails--organisation: {}", organisation);
        log.debug("UserController--getMainPageDetails--OUT");
        return ResponseEntity.ok().body(organisation);
    }

}
