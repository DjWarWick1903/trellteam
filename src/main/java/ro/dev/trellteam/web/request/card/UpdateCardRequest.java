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
    @NotNull(message = "Card id must be specified.")
    @JsonProperty("cardId")
    private Long cardId;

    @NotNull(message = "Card title must be specified.")
    @JsonProperty("title")
    private String title;

    @NotNull(message = "Card type id must be specified.")
    @JsonProperty("typeId")
    private Long typeId;

    @NotNull(message = "Card urgency must be specified.")
    @JsonProperty("urgency")
    private String urgency;

    @NotNull(message = "Card difficulty must be specified.")
    @JsonProperty("difficulty")
    private String difficulty;

    @NotNull(message = "Card description must be specified.")
    @NotEmpty(message = "Card description cannot be empty.")
    @JsonProperty("description")
    private String description;

    @JsonProperty("notes")
    private String notes;

    @NotNull(message = "Must specify which card property was changed.")
    @JsonProperty("changed")
    private String changed;

    @NotNull(message = "User name which changed card details must be specified.")
    @NotEmpty(message = "User name which changed card details must not be empty.")
    @JsonProperty("username")
    private String username;
}
