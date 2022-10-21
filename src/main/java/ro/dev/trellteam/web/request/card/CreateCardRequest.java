package ro.dev.trellteam.web.request.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.dev.trellteam.web.dto.CardDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class CreateCardRequest {
    @NotNull(message = "The ID of the board must be specified.")
    @JsonProperty("boardId")
    private Long boardId;

    @NotNull(message = "The ID of the card type must be specified.")
    @JsonProperty("typeId")
    private Long typeId;

    @Valid
    @NotNull(message = "Card details must be specified.")
    @JsonProperty("card")
    private CardDto card;
}
