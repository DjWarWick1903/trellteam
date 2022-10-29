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
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("boardId")
    private Long boardId;

    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("typeId")
    private Long typeId;

    @Valid
    @NotNull(message = "TRELL_ERR_8")
    @JsonProperty("card")
    private CardDto card;
}
