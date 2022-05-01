package ro.dev.trellteam.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class SecurityHelper {

    private final static String SECRETKEY = "secret";

    /**
     * Method used to generate an JWT access token customized from a user and requestURL.
     * @param user
     * @param requestURL
     * @return String
     */
    public static String generateAccessToken(final User user, final String requestURL) {
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
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
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
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
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(requestURL)
                .sign(algorithm);
    }

    /**
     * Method used to decode a JWT refresh token.
     * @param refresh_token
     * @return DecodedJWT
     */
    public static DecodedJWT decodeJWT(final String refresh_token) {
        final Algorithm algorithm = Algorithm.HMAC256(SECRETKEY.getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(refresh_token);
    }

    /**
     * Method used to convert a String array of roles to a collection of authorities.
     * @param roles
     * @return Collection
     */
    public static Collection<SimpleGrantedAuthority> getGrants(String[] roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }

    /**
     * Method used to convert a collection of roles to a collection of authorities.
     * @param roles
     * @return Collection
     */
    public static Collection<SimpleGrantedAuthority> getGrants(List<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }
}
