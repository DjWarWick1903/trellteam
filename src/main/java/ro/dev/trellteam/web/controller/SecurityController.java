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
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.AccountDto;
import ro.dev.trellteam.web.dto.RoleDto;
import ro.dev.trellteam.web.request.security.RoleToUserForm;
import ro.dev.trellteam.web.response.ObjectResponse;
import ro.dev.trellteam.web.service.RoleService;
import ro.dev.trellteam.web.service.SecurityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/security/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SecurityController {
    private final RoleService roleService;
    private final SecurityService securityService;

    @PutMapping("/account/role/add")
    public ResponseEntity<ObjectResponse> addRoleToAccount(@RequestBody @Valid RoleToUserForm form) {
        log.debug("SecurityController--addRoleToAccount--IN");
        final AccountDto accountDto = securityService.addRoleToAccount(form);
        final ObjectResponse response = new ObjectResponse(accountDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/account/role/remove")
    public ResponseEntity<ObjectResponse> removeRoleFromAccount(@RequestBody @Valid RoleToUserForm form) {
        log.debug("SecurityController--removeRoleFromAccount--IN");
        final AccountDto accountDto = securityService.removeRoleFromAccount(form);
        final ObjectResponse response = new ObjectResponse(accountDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/role")
    public ResponseEntity<ObjectResponse> getRoles() {
        log.debug("SecurityController--getRoles--IN");
        final List<RoleDto> roleDtos = securityService.getRoles();
        final ObjectResponse response = new ObjectResponse(roleDtos);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/account/role/{username}")
    public ResponseEntity<ObjectResponse> getAccountRoles(@PathVariable String username) {
        log.debug("SecurityController--getAccountRoles--IN");
        if(username == null || username.isEmpty()) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final List<RoleDto> roleDtos = securityService.getAccountRoles(username);
        final ObjectResponse response = new ObjectResponse(roleDtos);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/role")
    public ResponseEntity<ObjectResponse> createRole(@RequestBody @Valid RoleDto payload) {
        log.debug("SecurityController--createRole--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/security/v1/role").toUriString());
        final RoleDto role = securityService.saveRole(payload);
        final ObjectResponse response = new ObjectResponse(role);
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity deleteRole(@PathVariable Long id) {
        log.debug("SecurityController--deleteRole--IN");
        roleService.deleteRoleById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("SecurityController--refreshToken--IN");
        securityService.refreshToken(request, response);
    }
}