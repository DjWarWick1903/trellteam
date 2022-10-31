package ro.dev.trellteam.web.request.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class RoleToUserForm {
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("username")
    private String username;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("roleID")
    private Long roleID;
}
