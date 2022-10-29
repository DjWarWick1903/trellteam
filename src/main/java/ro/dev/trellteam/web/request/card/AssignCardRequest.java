package ro.dev.trellteam.web.request.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class AssignCardRequest {
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("id")
    private Long cardId;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("username")
    private String username;
}
