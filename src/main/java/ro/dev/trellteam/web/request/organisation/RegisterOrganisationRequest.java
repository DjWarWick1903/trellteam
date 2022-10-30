package ro.dev.trellteam.web.request.organisation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.dev.trellteam.web.dto.AccountDto;
import ro.dev.trellteam.web.dto.OrganisationDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class RegisterOrganisationRequest {
    @Valid
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("organisation")
    private OrganisationDto organisationDto;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("organisation")
    private String departmentName;
    @Valid
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("organisation")
    private AccountDto accountDto;
}
