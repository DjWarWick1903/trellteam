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
public class AddCardCommentRequest {
    @NotNull(message = "The ID of the card must be specified.")
    @JsonProperty("id")
    private Long cardId;

    @NotNull(message = "The username of user which requests the card status change must be specified.")
    @JsonProperty("username")
    private String username;

    @NotNull(message = "The comment must be specified.")
    @NotEmpty(message = "The comment cannot be empty.")
    @JsonProperty("comment")
    private String comment;
}
