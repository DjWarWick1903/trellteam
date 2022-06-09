package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Board;
import ro.dev.trellteam.model.Card;
import ro.dev.trellteam.model.Type;
import ro.dev.trellteam.service.*;

import java.net.URI;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/card", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CardController {

    private final DepartmentService departmentService;
    private final TypeService typeService;
    private final BoardService boardService;
    private final AccountService accountService;
    private final CardService cardService;
    private final TransactionalOperations transactionalOperations;


    @PostMapping("/main")
    public ResponseEntity<Card> createTicket(@RequestBody Map<String, String> payload) {
        log.debug("BoardController--createBoard--IN");

        final String title = payload.get("title");
        final Long typeId = Long.parseLong(payload.get("typeId"));
        final String difficulty = payload.get("difficulty");
        final String description = payload.get("description");
        final Long boardId = Long.parseLong(payload.get("boardId"));
        final String publisherUsername = payload.get("username");

        final Account publisher = accountService.getAccount(publisherUsername);
        final Type type = typeService.findById(typeId);

        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setDifficulty(difficulty);
        card.setPublisher(publisher);
        card.setStatus("TO DO");
        card.setType(type);

        Board board = boardService.getBoardById(boardId);

        card = transactionalOperations.createCard(board, card);

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/main").toUriString());
        log.debug("BoardController--createBoard--uri: {}", uri);

        log.debug("BoardController--createBoard--OUT");
        return ResponseEntity.created(uri).body(card);
    }
}
