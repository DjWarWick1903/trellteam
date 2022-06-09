package ro.dev.trellteam.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ro.dev.trellteam.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class SecurityHelper {

    private final static String SECRETKEY = "secret";
    private final static int jwtExpirationMs = 3600000;
    private final static int jwtRefreshExpirationMs = 86400000;

    private static final Map<String, String[]> endpointPrivileges;

    static {
        endpointPrivileges = new HashMap<>();

        //permit all
        endpointPrivileges.put("/security/login/**", new String[] {"ALL"});
        endpointPrivileges.put("/security/token/refresh", new String[] {"ALL"});
        endpointPrivileges.put("/security/organisation/register", new String[] {"ALL"});

        //ADMIN only
        endpointPrivileges.put("/security/account/**", new String[] {"ADMIN"});
        endpointPrivileges.put("/security/role/**", new String[] {"ADMIN", "MANAGER"});

        //Others
        endpointPrivileges.put("/security/ping", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
        endpointPrivileges.put("/user/**", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
        endpointPrivileges.put("/board/**", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
        endpointPrivileges.put("/card/**", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
        endpointPrivileges.put("/organisation/**", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
        endpointPrivileges.put("/employee/**", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
        endpointPrivileges.put("/department/**", new String[] {"ADMIN", "MANAGER", "DEVOPS", "DEV"});
    }

    public static Map<String, String[]> getEndpointPrivileges() { return endpointPrivileges; }

    /**
     * Method used to generate an JWT access token customized from a user and requestURL.
     * @param user
     * @param requestURL
     * @return String
     */
    public static String generateAccessToken(final User user, final String requestURL) {
        log.debug("SecurityHelper--generateAccessToken--IN");
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withIssuer(requestURL)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    /**
     * Method used to generate an JWT access token customized from an account and requestURL.
     * @param account
     * @param requestURL
     * @return String
     */
    public static String generateAccessToken(final Account account, final String requestURL) {
        log.debug("SecurityHelper--generateAccessToken--IN");
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withIssuer(requestURL)
                .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
    }

    /**
     * Method used to generate an JWT access token customized from a username and requestURL.
     * @param username
     * @param requestURL
     * @return String
     */
    public static String generateRefreshToken(final String username, final String requestURL) {
        log.debug("SecurityHelper--generateRefreshToken--IN");
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .withIssuer(requestURL)
                .sign(algorithm);
    }

    /**
     * Method used to decode a JWT refresh token.
     * @param refresh_token
     * @return DecodedJWT
     */
    public static DecodedJWT decodeJWT(final String refresh_token) {
        log.debug("SecurityHelper--decodeJWT--IN");
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        log.debug("SecurityHelper--decodeJWT--OUT");
        return verifier.verify(refresh_token);
    }

    /**
     * Method used to convert a String array of roles to a collection of authorities.
     * @param roles
     * @return Collection
     */
    public static Collection<SimpleGrantedAuthority> getGrants(String[] roles) {
        log.debug("SecurityHelper--getGrants--IN");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        log.debug("SecurityHelper--getGrants--OUT");
        return authorities;
    }

    /**
     * Method used to convert a collection of roles to a collection of authorities.
     * @param roles
     * @return Collection
     */
    public static Collection<SimpleGrantedAuthority> getGrants(List<Role> roles) {
        log.debug("SecurityHelper--getGrants--IN");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        log.debug("SecurityHelper--getGrants--OUT");
        return authorities;
    }
}
