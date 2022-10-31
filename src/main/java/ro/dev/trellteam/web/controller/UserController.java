package ro.dev.trellteam.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.web.dto.AccountDto;
import ro.dev.trellteam.web.mapper.AccountMapper;
import ro.dev.trellteam.web.response.ObjectResponse;
import ro.dev.trellteam.web.service.AccountService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/main/account/{username}")
    public ResponseEntity<ObjectResponse> fetchAccount(@PathVariable String username) {
        log.debug("UserController--fetchAccount--IN");
        final Account account = accountService.getAccount(username);
        final ObjectResponse response = new ObjectResponse(accountMapper.domainToDto(account));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/main/account")
    public ResponseEntity<ObjectResponse> createAccount(@RequestBody AccountDto request) {
        log.debug("UserController--createAccount--IN");
        final Account account = accountService.save(accountMapper.dtoToDomain(request), true);
        final ObjectResponse response = new ObjectResponse(accountMapper.domainToDto(account));
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/v1/main/account").toUriString());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/account")
    public ResponseEntity<ObjectResponse> getAccounts() {
        log.debug("UserController--getAccounts--IN");
        final List<Account> accounts = accountService.list();
        final List<AccountDto> accountDtos = accounts.stream()
                .map(a -> accountMapper.domainToDto(a))
                .collect(Collectors.toList());
        final ObjectResponse response = new ObjectResponse(accountDtos);
        return ResponseEntity.ok().body(response);
    }
}
