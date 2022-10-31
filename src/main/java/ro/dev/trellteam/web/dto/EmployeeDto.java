package ro.dev.trellteam.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    @JsonProperty("id")
    private Long id;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("firstName")
    private String firstName;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phone")
    private String phone;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("cnp")
    private String CNP;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("birthday")
    private Date bday;
}
