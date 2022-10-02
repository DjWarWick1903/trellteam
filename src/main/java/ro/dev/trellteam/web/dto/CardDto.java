package ro.dev.trellteam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;
    private String title;
    private String difficulty;
    private String description;
    private String notes;
    private String status;
    private String urgency;
    private AccountDto publisher;
    private AccountDto assigned;
    private TypeDto type;
    private Set<CommentDto> comments;
    private Set<CardLogDto> logs;
}
