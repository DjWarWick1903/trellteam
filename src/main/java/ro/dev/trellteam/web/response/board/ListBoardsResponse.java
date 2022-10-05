package ro.dev.trellteam.web.response.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ro.dev.trellteam.web.dto.BoardDto;
import ro.dev.trellteam.web.response.Response;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListBoardsResponse extends Response {
    private List<BoardDto> boards;
}
