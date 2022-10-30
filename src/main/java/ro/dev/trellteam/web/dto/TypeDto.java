package ro.dev.trellteam.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDto {
    @JsonProperty("id")
    private Long id;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("name")
    private String name;
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("idOrganisation")
    private Long idOrganisation;
}
