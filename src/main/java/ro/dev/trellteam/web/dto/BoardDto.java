package ro.dev.trellteam.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    @NotNull(message = "TRELL_ERR_8")
    private String title;

    @JsonProperty("dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateCreated;

    @JsonProperty("departmentId")
    @NotNull(message = "TRELL_ERR_8")
    private Long idDep;

    @JsonProperty("version")
    @NotNull(message = "TRELL_ERR_8")
    private String version;

    @JsonProperty("releaseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "TRELL_ERR_8")
    private Date release;

    @JsonProperty("cards")
    private List<CardDto> cards;
}
