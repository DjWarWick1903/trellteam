package ro.dev.trellteam.web.response.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ro.dev.trellteam.web.dto.BoardDto;
import ro.dev.trellteam.web.response.Response;

@Getter
@AllArgsConstructor
public class CreateBoardResponse extends Response {
    private BoardDto board;
}
