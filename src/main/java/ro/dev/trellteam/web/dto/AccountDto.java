package ro.dev.trellteam.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @JsonProperty("id")
    private Long id;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("username")
    private String username;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("email")
    private String email;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("password")
    private String password;
    @JsonProperty("dateCreated")
    private Date dateCreated;
    @JsonProperty("disabled")
    private Integer disabled;
    @Valid
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("employee")
    private EmployeeDto employee;
    @JsonProperty("roles")
    private List<RoleDto> roles;
}
