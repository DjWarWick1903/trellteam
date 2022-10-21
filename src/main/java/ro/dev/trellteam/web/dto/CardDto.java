package ro.dev.trellteam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.dev.trellteam.enums.CardStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;

    @NotNull(message = "Title must be specified.")
    private String title;

    private String difficulty;

    @NotNull(message = "Description must be specified.")
    private String description;

    private String notes;

    private CardStatusEnum status;

    @NotNull(message = "Card urgency must be specified.")
    private String urgency;

    @NotNull(message = "Publisher details must be specified.")
    private AccountDto publisher;
    private AccountDto assigned;

    private TypeDto type;

    private Set<CommentDto> comments;
    private Set<CardLogDto> logs;
}
