package ro.dev.trellteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.model.*;
import ro.dev.trellteam.service.*;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Set;

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
    private final CardLogService cardLogService;
    private final CommentService commentService;
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

        CardLog cardLog = new CardLog();
        cardLog.setUser(publisher);
        cardLog.setLogDate(new Date());
        cardLog.setText("Ticket created");

        card = transactionalOperations.createCard(board, card, cardLog);

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/main").toUriString());
        log.debug("CardController--createTicket--uri: {}", uri);

        log.debug("CardController--createTicket--OUT");
        return ResponseEntity.created(uri).body(card);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<Card> getTicketById(@PathVariable Long id) {
        log.debug("CardController--getTicketById--IN");
        log.debug("CardController--getTicketById--id: {}", id);

        final Card card = cardService.findById(id);

        log.debug("CardController--getTicketById--card: {}", card);
        log.debug("CardController--getTicketById--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/todo")
    public ResponseEntity<Card> updateTicketInToDo(@RequestBody Map<String, String> payload) {
        log.debug("CardController--updateTicketInToDo--IN");

        final String username = payload.get("username");
        final Long idCard = Long.parseLong(payload.get("id"));

        log.debug("CardController--updateTicketInToDo--username: {}", username);
        log.debug("CardController--updateTicketInToDo--idCard: {}", idCard);

        Card card = cardService.findById(idCard);

        final Account account = accountService.getAccount(username);
        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(account);
        cardLog.setText("Status: " + card.getStatus() + " ---> TO DO");

        card = transactionalOperations.changeCardStatus(card, cardLog, "TO DO");

        log.debug("CardController--updateTicketInToDo--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/progress")
    public ResponseEntity<Card> updateTicketInProgress(@RequestBody Map<String, String> payload) {
        log.debug("CardController--updateTicketInProgress--IN");

        final String username = payload.get("username");
        final Long idCard = Long.parseLong(payload.get("id"));

        log.debug("CardController--updateTicketInProgress--username: {}", username);
        log.debug("CardController--updateTicketInProgress--idCard: {}", idCard);

        Card card = cardService.findById(idCard);

        final Account account = accountService.getAccount(username);
        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(account);
        cardLog.setText("Status: " + card.getStatus() + " ---> IN PROGRESS");

        card = transactionalOperations.changeCardStatus(card, cardLog, "IN PROGRESS");

        log.debug("CardController--updateTicketInProgress--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/done")
    public ResponseEntity<Card> updateTicketInDone(@RequestBody Map<String, String> payload) {
        log.debug("CardController--updateTicketInDone--IN");

        final String username = payload.get("username");
        final Long idCard = Long.parseLong(payload.get("id"));

        log.debug("CardController--updateTicketInDone--username: {}", username);
        log.debug("CardController--updateTicketInDone--idCard: {}", idCard);

        Card card = cardService.findById(idCard);

        final Account account = accountService.getAccount(username);
        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(account);
        cardLog.setText("Status: " + card.getStatus() + " ---> DONE");

        card = transactionalOperations.changeCardStatus(card, cardLog, "DONE");

        log.debug("CardController--updateTicketInDone--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main")
    public ResponseEntity<Card> updateTicket(@RequestBody Map<String, String> payload) {
        log.debug("CardController--updateTicket--IN");
        log.debug("CardController--updateTicket--payload: {}", payload);

        final Long id = Long.parseLong(payload.get("id"));
        final String title = payload.get("title");
        final Long typeId = Long.parseLong(payload.get("typeId"));
        final String urgency = payload.get("urgency");
        final String difficulty = payload.get("difficulty");
        final String description = payload.get("description");
        final String notes = payload.get("notes");
        final String changed = payload.get("changed");
        final String username = payload.get("username");

        Card oldCard = cardService.findById(id);

        // new object with which we compare the old one and get the log
        Card newCard = new Card();
        newCard.setTitle(title);
        final Type type = typeService.findById(typeId);
        newCard.setType(type);
        newCard.setUrgency(urgency);
        newCard.setDifficulty(difficulty);
        newCard.setDescription(description);
        newCard.setNotes(notes);

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(accountService.getAccount(username));
        cardLog.setText(GeneralHelper.getCardLogText(oldCard, newCard, changed));

        //update old card object
        oldCard.setTitle(title);
        oldCard.setType(type);
        oldCard.setUrgency(urgency);
        oldCard.setDifficulty(difficulty);
        oldCard.setDescription(description);
        oldCard.setNotes(notes);

        oldCard = transactionalOperations.updateCard(oldCard, cardLog);

        log.debug("CardController--updateTicket--OUT");
        return ResponseEntity.ok().body(oldCard);
    }

    @PutMapping("/assign")
    public ResponseEntity<Card> assignTicket(@RequestBody Map<String, String> payload) {
        log.debug("CardController--assignTicket--IN");
        log.debug("CardController--assignTicket--payload: {}", payload);

        final Long idTicket = Long.parseLong(payload.get("id"));
        final String username = payload.get("user");

        Card card = cardService.findById(idTicket);
        final Account newAsignee = accountService.getAccount(username);
        final Account oldAsignee = card.getAssigned();
        final String pastUsername = oldAsignee == null ? "Undefined" : oldAsignee.getUsername();

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(newAsignee);
        cardLog.setText("Assigned: " + pastUsername + " ---> " + username);

        card.setAssigned(newAsignee);
        card = transactionalOperations.assignCard(card, cardLog);

        log.debug("CardController--assignTicket--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/unassign/{id}")
    public ResponseEntity<Card> unassignTicket(@PathVariable Long id) {
        log.debug("CardController--unassignTicket--IN");
        log.debug("CardController--unassignTicket--id: {}", id);

        Card card = cardService.findById(id);

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(card.getAssigned());
        cardLog.setText("Assigned: " + card.getAssigned().getUsername() + " ---> Undefined");

        card.setAssigned(null);
        card = transactionalOperations.unassignCard(card, cardLog);

        log.debug("CardController--unassignTicket--OUT");
        return ResponseEntity.ok().body(card);
    }

    @GetMapping("/comment/{idCard}")
    public ResponseEntity<Set<Comment>> getComments(@PathVariable Long idCard) {
        log.debug("CardController--addCardComment--IN");
        log.debug("CardController--addCardComment--idCard: {}", idCard);

        final Card card = cardService.findById(idCard);

        log.debug("CardController--addCardComment--comments: {}", card.getComments());
        log.debug("CardController--addCardComment--OUT");

        return ResponseEntity.ok().body(card.getComments());
    }

    @PostMapping("/comment")
    public ResponseEntity<Card> addCardComment(@RequestBody Map<String, String> payload) {
        log.debug("CardController--addCardComment--IN");

        final String username = payload.get("username");
        final String commentText = payload.get("comment");
        final Long idCard = Long.parseLong(payload.get("idCard"));

        final Account user = accountService.getAccount(username);
        Card card = cardService.findById(idCard);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentDate(new Date());
        comment.setText(commentText);

        CardLog cardLog = new CardLog();
        cardLog.setUser(user);
        cardLog.setLogDate(new Date());
        cardLog.setText("User " + user.getUsername() + " added comment: " + commentText);

        card = transactionalOperations.createCardComment(card, comment, cardLog);

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/comment").toUriString());
        log.debug("CardController--addCardComment--uri: {}", uri);

        log.debug("CardController--addCardComment--OUT");
        return ResponseEntity.created(uri).body(card);
    }
}
