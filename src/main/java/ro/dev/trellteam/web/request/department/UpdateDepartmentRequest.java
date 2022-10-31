package ro.dev.trellteam.web.request.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDepartmentRequest {
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("idDepartment")
    private Long idDepartment;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("name")
    private String name;
    @JsonProperty("idManager")
    private Long idManager;
}
