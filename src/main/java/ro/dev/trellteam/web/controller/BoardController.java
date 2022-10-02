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
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.web.service.BoardService;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/board", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/main")
    public ResponseEntity<Board> createBoard(@RequestBody Map<String, String> payload) {
        log.debug("BoardController--createBoard--IN");

        final Long idDep = Long.parseLong(payload.get("idDep"));
        final String title = payload.get("title");
        final String version = payload.get("version");
        final String release = payload.get("release");

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/board/main").toUriString());
        log.debug("BoardController--createBoard--uri: {}", uri);

        log.debug("BoardController--createBoard--idDep: {}", idDep);
        log.debug("BoardController--createBoard--title: {}", title);
        log.debug("BoardController--createBoard--version: {}", version);
        log.debug("BoardController--createBoard--release: {}", release);

        Board board = new Board();
        board.setDateCreated(new java.sql.Date((new Date()).getTime()));
        board.setTitle(title);
        board.setIdDep(idDep);
        board.setVersion(version);
        board.setRelease(new java.sql.Date(GeneralHelper.convertStringToDate(release, "yyyy-MM-dd").getTime()));

        log.debug("BoardController--createBoard--board: {}", board);

        board = boardService.createBoard(board);

        log.debug("BoardController--createBoard--board: {}", board.toString());
        log.debug("BoardController--createBoard--OUT");

        return ResponseEntity.created(uri).body(board);
    }

    @GetMapping("/department/{idDepartment}")
    public ResponseEntity<List<Board>> listBoardsByDepartment(@PathVariable Long idDepartment) {
        log.debug("BoardController--listBoardsByDepartment--IN");
        log.debug("BoardController--listBoardsByDepartment--idDepartment: {}", idDepartment);

        final List<Board> boards = boardService.listDepartmentBoards(idDepartment);

        log.debug("BoardController--listBoardsByDepartment--boards: {}", boards.toString());
        log.debug("BoardController--listBoardsByDepartment--OUT");

        return ResponseEntity.ok().body(boards);
    }
}
