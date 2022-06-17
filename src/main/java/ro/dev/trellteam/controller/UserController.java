package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.model.*;
import ro.dev.trellteam.service.*;

import java.net.URI;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {
    private final AccountService accountService;

    @GetMapping("/main/account/{username}")
    public ResponseEntity<Account> fetchAccount(@PathVariable String username) {
        log.debug("UserController--fetchAccount--IN");
        log.debug("UserController--fetchAccount--username: {}", username);

        final Account account = accountService.getAccount(username);

        log.debug("UserController--fetchAccount--account: {}", account.toString());
        log.debug("UserController--fetchAccount--OUT");
        return ResponseEntity.ok().body(account);
    }

    @PostMapping("/main/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        log.debug("UserController--createAccount--IN");

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/main/account").toUriString());
        log.debug("UserController--createAccount--uri: {}", uri);

        log.debug("UserController--createAccount--account: {}", account.toString());
        log.debug("UserController--createAccount--OUT");
        return ResponseEntity.created(uri).body(accountService.save(account, true));
    }

    @GetMapping("/account")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok().body(accountService.list());
    }
}
