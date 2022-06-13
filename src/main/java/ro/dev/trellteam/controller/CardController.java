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
        log.debug("CardController--createTicket--IN");

        final String title = payload.get("title");
        final Long typeId = Long.parseLong(payload.get("typeId"));
        final String urgency = payload.get("urgency");
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
        card.setUrgency(urgency);

        Board board = boardService.getBoardById(boardId);

        card = transactionalOperations.createCard(board, card);

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/main").toUriString());
        log.debug("CardController--createTicket--uri: {}", uri);

        log.debug("CardController--createTicket--OUT");
        return ResponseEntity.created(uri).body(card);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        log.debug("CardController--getTicketById--IN");
        log.debug("CardController--getTicketById--id: {}", id);

        final Card card = cardService.findById(id);

        log.debug("CardController--getTicketById--card: {}", card);
        log.debug("CardController--getTicketById--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/todo/{id}")
    public ResponseEntity<?> updateTicketInToDo(@PathVariable Long id) {
        log.debug("CardController--updateTicketInToDo--IN");
        log.debug("CardController--updateTicketInToDo--id: {}", id);

        Card card = cardService.findById(id);
        card.setStatus("TO DO");

        card = cardService.save(card);

        log.debug("CardController--updateTicketInToDo--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/progress/{id}")
    public ResponseEntity<?> updateTicketInProgress(@PathVariable Long id) {
        log.debug("CardController--updateTicketInProgress--IN");
        log.debug("CardController--updateTicketInProgress--id: {}", id);

        Card card = cardService.findById(id);
        card.setStatus("IN PROGRESS");

        card = cardService.save(card);

        log.debug("CardController--updateTicketInProgress--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/done/{id}")
    public ResponseEntity<?> updateTicketInDone(@PathVariable Long id) {
        log.debug("CardController--updateTicketInDone--IN");
        log.debug("CardController--updateTicketInDone--id: {}", id);

        Card card = cardService.findById(id);
        card.setStatus("DONE");

        card = cardService.save(card);

        log.debug("CardController--updateTicketInDone--OUT");
        return ResponseEntity.ok().body(card);
    }
//<h3 class="card-title p-2" id="title"></h3>
    @PutMapping("/main")
    public ResponseEntity<?> updateTicket(@RequestBody Map<String, String> payload) {
        log.debug("CardController--updateTicket--IN");
        log.debug("CardController--updateTicket--payload: {}", payload);

        final Long id = Long.parseLong(payload.get("id"));
        final String title = payload.get("title");
        final Long typeId = Long.parseLong(payload.get("typeId"));
        final String urgency = payload.get("urgency");
        final String difficulty = payload.get("difficulty");
        final String description = payload.get("description");
        final String notes = payload.get("notes");

        Card card = cardService.findById(id);
        card.setTitle(title);
        final Type type = typeService.findById(typeId);
        card.setType(type);
        card.setUrgency(urgency);
        card.setDifficulty(difficulty);
        card.setDescription(description);
        card.setNotes(notes);

        card = cardService.save(card);

        log.debug("CardController--updateTicket--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/assign")
    public ResponseEntity<?> assignTicket(@RequestBody Map<String, String> payload) {
        log.debug("CardController--assignTicket--IN");
        log.debug("CardController--assignTicket--payload: {}", payload);

        final Long idTicket = Long.parseLong(payload.get("id"));
        final String username = payload.get("user");

        Card card = cardService.findById(idTicket);
        final Account account = accountService.getAccount(username);
        card.setAssigned(account);

        card = cardService.save(card);

        log.debug("CardController--assignTicket--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/unassign/{id}")
    public ResponseEntity<?> unassignTicket(@PathVariable Long id) {
        log.debug("CardController--unassignTicket--IN");
        log.debug("CardController--unassignTicket--id: {}", id);

        Card card = cardService.findById(id);
        card.setAssigned(null);

        card = cardService.save(card);

        log.debug("CardController--unassignTicket--OUT");
        return ResponseEntity.ok().body(card);
    }
}
