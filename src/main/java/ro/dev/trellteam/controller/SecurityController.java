package ro.dev.trellteam.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Role;
import ro.dev.trellteam.service.AccountService;
import ro.dev.trellteam.service.RoleService;

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
public class SecurityController {
    private final AccountService accountService;
    private final RoleService roleService;

    @GetMapping("/account/all")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok().body(accountService.list());
    }

    @GetMapping("/account")
    public ResponseEntity<Account> getAccount(@RequestBody Map<String, String> payload) {
        final String username = payload.get("username");
        return ResponseEntity.ok().body(accountService.getAccount(username));
    }

    @PostMapping("/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/security/account").toUriString());
        return ResponseEntity.created(uri).body(accountService.saveAccount(account));
    }

    @PostMapping("/account/register")
    public ResponseEntity<Account> registerAdminAccount(@RequestBody Account account) {
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/security/account/register").toUriString());
        final Role role = roleService.findByName("ADMIN");
        account.addRole(role);
        return ResponseEntity.created(uri).body(accountService.saveAccount(account));
    }

    //pathvariable
    @PutMapping ("/account/role")
    public ResponseEntity<Account> addRoleToAccount(@RequestBody RoleToUserForm form) {
        final Role role = roleService.findById(form.getRoleID());
        Account account = accountService.getAccount(form.getUsername());
        account.addRole(role);
        return ResponseEntity.ok().body(accountService.saveAccount(account));
    }

    @GetMapping("/role")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok().body(roleService.list());
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