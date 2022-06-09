package ro.dev.trellteam.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.security.LoginDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.debug("CustomAuthenticationFilter--attemptAuthentication--IN");
        String username;
        String password;

        try {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            username = requestMap.get("username");
            password = requestMap.get("password");
        } catch(IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        log.debug("CustomAuthenticationFilter--attemptAuthentication--Username: {}", username);
        log.debug("CustomAuthenticationFilter--attemptAuthentication--Password: {}", password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.debug("CustomAuthenticationFilter--attemptAuthentication--authenticationToken: {}", authenticationToken);
        log.debug("CustomAuthenticationFilter--attemptAuthentication--OUT");
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.debug("CustomAuthenticationFilter--successfulAuthentication--IN");
        final User user = (User) authentication.getPrincipal();
        final String access_token = SecurityHelper.generateAccessToken(user, request.getRequestURL().toString());
        final String refresh_token = SecurityHelper.generateRefreshToken(user.getUsername(), request.getRequestURL().toString());
        /*response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);*/

        final List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        log.debug("CustomAuthenticationFilter--successfulAuthentication--user: {}", user);
        log.debug("CustomAuthenticationFilter--successfulAuthentication--roles: {}", roles);
        final LoginDetails loginDetails = new LoginDetails(access_token, refresh_token, roles);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), loginDetails);
        log.debug("CustomAuthenticationFilter--successfulAuthentication--OUT");
    }
}
