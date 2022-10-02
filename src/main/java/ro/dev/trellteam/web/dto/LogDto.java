package ro.dev.trellteam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {
    private Long id;
    private String description;
    private Long idAccount;
}
