package ro.dev.trellteam.web.request.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.dev.trellteam.web.dto.AccountDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class CreateEmployeeRequest {
    @Valid
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("account")
    private AccountDto account;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("idDepartment")
    private Long idDepartment;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("idRole")
    private Long idRole;
}
