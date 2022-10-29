package ro.dev.trellteam.web.request.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class AssignEmployeeRequest {
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("idOrganisation")
    private Long idOrganisation;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("idEmployee")
    private Long idEmployee;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("departmentName")
    private String departmentName;
}
