package ro.dev.trellteam.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ro.dev.trellteam.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class SecurityHelper {

    private final static String SECRETKEY = "secret";

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
        log.debug("SecurityHelper--generateAccessToken--IN");
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
        log.debug("SecurityHelper--generateRefreshToken--IN");
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

    /**
     * Method used to extract Organisation object from a Map with String values.
     * @param organisationData
     * @return Organisation
     */
    public static Organisation getOrganisationFromMap(final Map<String, String> organisationData) {
        log.debug("SecurityHelper--getOrganisationFromMap--IN");
        Organisation organisation = new Organisation();
        organisation.setName(organisationData.get("name"));
        organisation.setCUI(organisationData.get("cui"));
        organisation.setDomain(organisationData.get("domain"));
        organisation.setSign(organisationData.get("sign"));
        organisation.setDateCreated(new Date());

        log.debug("SecurityHelper--getOrganisationFromMap--name: {}", organisation.getName());
        log.debug("SecurityHelper--getOrganisationFromMap--cui: {}", organisation.getCUI());
        log.debug("SecurityHelper--getOrganisationFromMap--domain: {}", organisation.getDomain());
        log.debug("SecurityHelper--getOrganisationFromMap--sign: {}", organisation.getSign());

        log.debug("SecurityHelper--getOrganisationFromMap--OUT");
        return organisation;
    }

    /**
     * Method used to extract Employee object from a Map with String values.
     * @param employeeData
     * @return Employee
     */
    public static Employee getEmployeeFromMap(final Map<String, String> employeeData) throws Exception {
        log.debug("SecurityHelper--getEmployeeFromMap--IN");
        Employee employee = new Employee();
        employee.setCNP(employeeData.get("cnp"));
        employee.setFirstName(employeeData.get("firstName"));
        employee.setLastName(employeeData.get("lastName"));
        employee.setPhone(employeeData.get("phone"));

        final String bdayString = employeeData.get("bday");
        final Date bday = new SimpleDateFormat("yyyy-MM-dd").parse(bdayString);
        employee.setBday(bday);

        log.debug("SecurityHelper--getEmployeeFromMap--firstName: {}", employee.getFirstName());
        log.debug("SecurityHelper--getEmployeeFromMap--lastName: {}", employee.getLastName());
        log.debug("SecurityHelper--getEmployeeFromMap--phone: {}", employee.getPhone());
        log.debug("SecurityHelper--getEmployeeFromMap--cnp: {}", employee.getCNP());
        log.debug("SecurityHelper--getEmployeeFromMap--bday: {}", employee.getBday());

        log.debug("SecurityHelper--getEmployeeFromMap--OUT");
        return employee;
    }

    /**
     * Method used to extract Account object from a Map with String values.
     * @param accountData
     * @return Account
     */
    public static Account getAccountFromMap(final Map<String, String> accountData) {
        log.debug("SecurityHelper--getAccountFromMap--IN");
        Account account = new Account();
        account.setEmail(accountData.get("email"));
        account.setUsername(accountData.get("username"));
        account.setPassword(accountData.get("password"));
        account.setDisabled(0);
        account.setDateCreated(new Date());

        log.debug("SecurityHelper--getAccountFromMap--email: {}", account.getEmail());
        log.debug("SecurityHelper--getAccountFromMap--username: {}", account.getUsername());
        log.debug("SecurityHelper--getAccountFromMap--password: {}", account.getPassword());
        log.debug("SecurityHelper--getAccountFromMap--OUT");
        return account;
    }
}
