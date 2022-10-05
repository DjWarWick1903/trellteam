package ro.dev.trellteam.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.web.dto.BoardDto;
import ro.dev.trellteam.web.mapper.BoardMapper;
import ro.dev.trellteam.web.response.board.CreateBoardResponse;
import ro.dev.trellteam.web.response.board.ListBoardsResponse;
import ro.dev.trellteam.web.service.BoardService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/board/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @PostMapping()
    public ResponseEntity<CreateBoardResponse> createBoard(@RequestBody @Valid final BoardDto requestBody) {
        log.debug("BoardController--createBoard--IN");

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/board/v1").toUriString());
        log.debug("BoardController--createBoard--uri: {}", uri);

        log.debug("BoardController--createBoard--idDep: {}", requestBody.getIdDep());
        log.debug("BoardController--createBoard--title: {}", requestBody.getTitle());
        log.debug("BoardController--createBoard--version: {}", requestBody.getVersion());
        log.debug("BoardController--createBoard--release: {}", requestBody.getRelease());

        Board board = boardMapper.dtoToDomain(requestBody);
        board.setDateCreated(new java.sql.Date((new Date()).getTime()));

        board = boardService.createBoard(board);
        log.debug("BoardController--createBoard--board: {}", board.toString());

        final CreateBoardResponse response = new CreateBoardResponse(boardMapper.domainToDto(board));

        log.debug("BoardController--createBoard--OUT");
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/department/{idDepartment}")
    public ResponseEntity<ListBoardsResponse> listBoardsByDepartment(@PathVariable Long idDepartment) {
        log.debug("BoardController--listBoardsByDepartment--IN");
        log.debug("BoardController--listBoardsByDepartment--idDepartment: {}", idDepartment);

        final List<Board> boards = boardService.listDepartmentBoards(idDepartment);
        log.debug("BoardController--listBoardsByDepartment--boards: {}", boards.toString());

        final ListBoardsResponse response = new ListBoardsResponse(
                boards.stream()
                        .map(boardMapper::domainToDto)
                        .collect(Collectors.toList())
        );

        log.debug("BoardController--listBoardsByDepartment--OUT");
        return ResponseEntity.ok().body(response);
    }
}
