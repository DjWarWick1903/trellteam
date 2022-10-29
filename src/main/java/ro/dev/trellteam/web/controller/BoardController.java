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
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.BoardDto;
import ro.dev.trellteam.web.mapper.BoardMapper;
import ro.dev.trellteam.web.response.ObjectResponse;
import ro.dev.trellteam.web.service.BoardService;

import javax.validation.Valid;
import java.net.URI;
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
    public ResponseEntity<ObjectResponse> createBoard(@RequestBody @Valid final BoardDto requestBody) {
        log.debug("BoardController--createBoard--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/board/v1").toUriString());
        log.debug("BoardController--createBoard--uri: {}", uri);

        final BoardDto board = boardService.createBoard(requestBody);
        final ObjectResponse response = new ObjectResponse(board);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/department/{idDepartment}")
    public ResponseEntity<ObjectResponse> listBoardsByDepartment(@PathVariable Long idDepartment) {
        log.debug("BoardController--listBoardsByDepartment--IN");
        if(idDepartment == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final List<Board> boards = boardService.listDepartmentBoards(idDepartment);
        final ObjectResponse response = new ObjectResponse(
                boards.stream()
                        .map(boardMapper::domainToDto)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok().body(response);
    }
}
