package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.web.dto.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto domainToDto(Account account);
    Account dtoToDomain(AccountDto accountDto);
}
