package ro.dev.trellteam.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.model.*;
import ro.dev.trellteam.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/security", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SecurityController {
    private final AccountService accountService;
    private final RoleService roleService;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().body(null);
    }

    //pathvariable
    @PutMapping ("/account/role/add")
    public ResponseEntity<Account> addRoleToAccount(@RequestBody RoleToUserForm form) {
        log.debug("SecurityController--addRoleToAccount--IN");
        log.debug("SecurityController--addRoleToAccount--form: {}", form);

        final Role role = roleService.findById(form.getRoleID());
        Account account = accountService.getAccount(form.getUsername());
        account.addRole(role);

        account = accountService.save(account, false);
        log.debug("SecurityController--addRoleToAccount--OUT");

        return ResponseEntity.ok().body(account);
    }

    @PutMapping("/account/role/remove")
    public ResponseEntity<Account> removeRoleFromAccount(@RequestBody RoleToUserForm form) {
        log.debug("SecurityController--removeRoleFromAccount--IN");
        log.debug("SecurityController--removeRoleFromAccount--form: {}", form);

        final Role role = roleService.findById(form.getRoleID());
        Account account = accountService.getAccount(form.getUsername());
        account.removeRole(role);

        account = accountService.save(account, false);
        log.debug("SecurityController--removeRoleFromAccount--OUT");

        return ResponseEntity.ok().body(account);
    }

    @GetMapping("/role")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok().body(roleService.list());
    }

    @GetMapping("/account/role/{username}")
    public ResponseEntity<List<Role>> getAccountRoles(@PathVariable String username) {
        log.debug("SecurityController--getAccountRoles--IN");
        log.debug("SecurityController--getAccountRoles--username: {}", username);
        final Account account = accountService.getAccount(username);
        final List<Role> roles = account.getRoles();
        log.debug("SecurityController--getAccountRoles--OUT");
        return ResponseEntity.ok().body(roles);
    }

    @PostMapping("/role")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/security/role").toUriString());
        return ResponseEntity.created(uri).body(roleService.save(role));
    }

    @DeleteMapping("/role")
    public void deleteRole(@RequestBody Map<String, Object> payload) {
        final Long idRole = (Long) payload.get("id");
        roleService.deleteRoleById(idRole);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Token: ")) {
            try {
                final String refresh_token = authorizationHeader.substring("Token: ".length());
                final DecodedJWT decodedJWT = SecurityHelper.decodeJWT(refresh_token);
                final String username = decodedJWT.getSubject();

                final Account account = accountService.getAccount(username);
                final String access_token = SecurityHelper.generateAccessToken(account, request.getRequestURL().toString());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch(final Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}

@Data
class RoleToUserForm {
    private String username;
    private Long roleID;
}