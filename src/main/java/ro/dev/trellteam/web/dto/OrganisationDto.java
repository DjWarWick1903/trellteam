package ro.dev.trellteam.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationDto {
    @JsonProperty("id")
    private Long id;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("name")
    private String name;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("sign")
    private String sign;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("cui")
    private String CUI;
    @JsonProperty("dateCreated")
    private Date dateCreated;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("departments")
    private Set<DepartmentDto> departments;
    @JsonProperty("employees")
    private Set<EmployeeDto> employees;
}
