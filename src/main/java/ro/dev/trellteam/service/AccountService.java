package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.helper.SecurityHelper;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Role;
import ro.dev.trellteam.repository.AccountRepository;
import ro.dev.trellteam.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        final Account account = accRepo.findByUsername(username);
        if(account == null) {
            log.error("AccountService--Account not found in the database");
            throw new UsernameNotFoundException("Account not found in the database");
        } else {
            log.info("AccountService--Account found in the database: {}", username);
        }

        final Collection<SimpleGrantedAuthority> authorities = SecurityHelper.getGrants(account.getRoles());

        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), authorities);
    }

    /**
     * Method used to save the account into the database.
     * @param account
     * @return Account
     */
    public Account saveAccount(Account account) {
        log.info("AccountService--Saving new account {} to the database", account.getUsername());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accRepo.save(account);
    }

    /**
     * Method used to add a new role to an account.
     * @param username
     * @param roleName
     */
    public void addRoleToAccount(final String username, final String roleName) {
        log.info("AccountService--Adding role {} to account {}", roleName, username);
        Account account = accRepo.findByUsername(username);
        final Role role = roleRepository.findByName(roleName);
        account.getRoles().add(role); //because we have the transactional annot, once the method finishes it will save into db
    }

    /**
     * Method used to get an account starting from a username.
     * @param username
     * @return Account
     */
    public Account getAccount(final String username) {
        log.info("AccountService--Fetching account {}" , username);
        return accRepo.findByUsername(username);
    }

    /**
     * Method used to return all Accounts
     * @return List
     */
    public List<Account> list() {
        log.info("AccountService--Fetching all accounts");
        return accRepo.findAll();
    }

    /**
     * Method used to delete an account
     * @param account
     */
    public void delete(Account account) {
        log.info("AccountService--Deleting account {}" , account.getUsername());
        accRepo.deleteById(account.getId());
    }
}
