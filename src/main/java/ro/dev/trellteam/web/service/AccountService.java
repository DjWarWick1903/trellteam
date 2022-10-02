package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Role;
import ro.dev.trellteam.web.repository.AccountRepository;
import ro.dev.trellteam.web.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService implements UserDetailsService {
    private final AccountRepository accRepo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //method used by Spring Security to find users
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug("AccountService--loadUserByUsername--IN");
        final Account account = accRepo.findByUsername(username);
        if(account == null) {
            log.error("AccountService--loadUserByUsername--Account not found in the database");
            throw new UsernameNotFoundException("Account not found in the database");
        } else {
            log.debug("AccountService--loadUserByUsername--Account found in the database: {}", username);
        }

        final Collection<SimpleGrantedAuthority> authorities = SecurityHelper.getGrants(account.getRoles());
        log.debug("AccountService--loadUserByUsername--OUT");

        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), authorities);
    }

    /**
     * Method used to save the account into the database.
     * @param account
     * @return Account
     */
    public Account save(Account account, boolean isNewAccount) {
        log.info("AccountService--save--IN");
        if(isNewAccount) account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accRepo.save(account);
        log.info("AccountService--save--OUT");

        return account;
    }

    public Account saveAndFlush(Account account) {
        log.info("AccountService--saveAndFlush--IN");
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accRepo.saveAndFlush(account);
        log.info("AccountService--saveAndFlush--OUT");

        return account;
    }

    /**
     * Method used to add a new role to an account.
     * @param username
     * @param roleName
     */
    public void addRoleToAccount(final String username, final String roleName) {
        log.info("AccountService--addRoleToAccount--IN");
        log.info("AccountService--addRoleToAccount--roleName: {}", roleName);
        log.info("AccountService--addRoleToAccount--username: {}", username);
        Account account = accRepo.findByUsername(username);
        final Role role = roleRepository.findByName(roleName);
        account.getRoles().add(role); //because we have the transactional annot, once the method finishes it will save into db
        log.info("AccountService--addRoleToAccount--OUT");
    }

    /**
     * Method used to get an account starting from a username.
     * @param username
     * @return Account
     */
    public Account getAccount(final String username) {
        log.info("AccountService--getAccount--IN");
        log.info("AccountService--getAccount--username: {}", username);
        final Account account = accRepo.findByUsername(username);
        log.info("AccountService--getAccount--OUT");
        return account;
    }

    /**
     * Method used to return all Accounts
     * @return List
     */
    public List<Account> list() {
        log.info("AccountService--list--IN");
        return accRepo.findAll();
    }

    /**
     * Method used to delete an account
     * @param account
     */
    public void delete(Account account) {
        log.info("AccountService--delete--IN");
        log.info("AccountService--delete--username: {}" , account.getUsername());
        accRepo.deleteById(account.getId());
        log.info("AccountService--delete--OUT");
    }
}
