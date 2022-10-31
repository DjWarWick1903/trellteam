package ro.dev.trellteam.web.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Role;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.web.dto.AccountDto;
import ro.dev.trellteam.web.dto.RoleDto;
import ro.dev.trellteam.web.mapper.AccountMapper;
import ro.dev.trellteam.web.mapper.RoleMapper;
import ro.dev.trellteam.web.request.security.RoleToUserForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SecurityService {
    private final RoleService roleService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final RoleMapper roleMapper;

    public AccountDto addRoleToAccount(RoleToUserForm request) {
        log.info("SecurityService--addRoleToAccount--request: {}", request);
        final Role role = roleService.findById(request.getRoleID());
        Account account = accountService.getAccount(request.getUsername());
        account.addRole(role);

        account = accountService.save(account, false);
        return accountMapper.domainToDto(account);
    }

    public AccountDto removeRoleFromAccount(RoleToUserForm request) {
        log.info("SecurityService--addRoleToAccount--request: {}", request);
        final Role role = roleService.findById(request.getRoleID());
        Account account = accountService.getAccount(request.getUsername());
        account.removeRole(role);

        account = accountService.save(account, false);
        return accountMapper.domainToDto(account);
    }

    public List<RoleDto> getRoles() {
        log.info("SecurityService--addRoleToAccount--IN");
        final List<Role> roles = roleService.list();
        return roles.stream()
                .map(r -> roleMapper.domainToDto(r))
                .collect(Collectors.toList());
    }

    public List<RoleDto> getAccountRoles(final String username) {
        log.info("SecurityService--getAccountRoles--username: {}", username);
        final Account account = accountService.getAccount(username);
        final List<Role> roles = account.getRoles();
        final List<RoleDto> roleDtos = roles.stream()
                .map(r -> roleMapper.domainToDto(r))
                .collect(Collectors.toList());
        return roleDtos;
    }

    public RoleDto saveRole(RoleDto request) {
        log.info("SecurityService--getAccountRoles--request: {}", request);
        final Role role = roleService.save(roleMapper.dtoToDomain(request));
        return roleMapper.domainToDto(role);
    }

    public void refreshToken(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("SecurityService--refreshToken--request: {}", request);
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Token: ")) {
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
            } catch (final Exception e) {
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
