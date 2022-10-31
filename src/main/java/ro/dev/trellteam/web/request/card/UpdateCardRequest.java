package ro.dev.trellteam.web.request.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCardRequest {
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("cardId")
    private Long cardId;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("title")
    private String title;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("typeId")
    private Long typeId;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("urgency")
    private String urgency;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("difficulty")
    private String difficulty;

    @NotNull(message = "TRELL_ERR_8")
    @NotEmpty(message = "TRELL_ERR_8")
    @JsonProperty("description")
    private String description;

    @JsonProperty("notes")
    private String notes;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("changed")
    private String changed;

    @NotNull(message = "TRELL_ERR_8")
    @NotEmpty(message = "TRELL_ERR_8")
    @JsonProperty("username")
    private String username;
}
