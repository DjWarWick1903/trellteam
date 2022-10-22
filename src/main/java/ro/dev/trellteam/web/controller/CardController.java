package ro.dev.trellteam.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.dev.trellteam.domain.Card;
import ro.dev.trellteam.domain.Comment;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.CardDto;
import ro.dev.trellteam.web.mapper.CardMapper;
import ro.dev.trellteam.web.request.card.AddCardCommentRequest;
import ro.dev.trellteam.web.request.card.AssignCardRequest;
import ro.dev.trellteam.web.request.card.CreateCardRequest;
import ro.dev.trellteam.web.request.card.UpdateCardStatusRequest;
import ro.dev.trellteam.web.request.card.UpdateCardRequest;
import ro.dev.trellteam.web.service.CardService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/card/v1", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping("/main")
    public ResponseEntity<CardDto> createTicket(@RequestBody @Valid CreateCardRequest payload) {
        log.debug("CardController--createTicket--IN");

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/v1/main").toUriString());
        log.debug("CardController--createTicket--uri: {}", uri);

        final CardDto card = cardService.createTicket(payload);

        log.debug("CardController--createTicket--OUT");
        return ResponseEntity.created(uri).body(card);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<CardDto> getTicketById(@PathVariable Long id) {
        log.debug("CardController--getTicketById--IN");
        log.debug("CardController--getTicketById--id: {}", id);

        final CardDto card = cardMapper.domainToDto(cardService.findById(id));

        log.debug("CardController--getTicketById--card: {}", card);
        log.debug("CardController--getTicketById--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/todo")
    public ResponseEntity<CardDto> updateTicketInToDo(@RequestBody @Valid UpdateCardStatusRequest payload) {
        log.debug("CardController--updateTicketInToDo--IN");
        log.debug("CardController--updateTicketInToDo--username: {}", payload.getUsername());
        log.debug("CardController--updateTicketInToDo--idCard: {}", payload.getCardId());

        final CardDto card = cardService.updateCardStatusInToDo(payload);

        log.debug("CardController--updateTicketInToDo--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/progress")
    public ResponseEntity<CardDto> updateTicketInProgress(@RequestBody @Valid UpdateCardStatusRequest payload) {
        log.debug("CardController--updateTicketInProgress--IN");
        log.debug("CardController--updateTicketInProgress--username: {}", payload.getUsername());
        log.debug("CardController--updateTicketInProgress--idCard: {}", payload.getCardId());

        final CardDto card = cardService.updateCardStatusInProgress(payload);

        log.debug("CardController--updateTicketInProgress--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main/done")
    public ResponseEntity<CardDto> updateTicketInDone(@RequestBody @Valid UpdateCardStatusRequest payload) {
        log.debug("CardController--updateTicketInDone--IN");
        log.debug("CardController--updateTicketInDone--username: {}", payload.getUsername());
        log.debug("CardController--updateTicketInDone--idCard: {}", payload.getCardId());

        final CardDto card = cardService.updateCardStatusInDone(payload);

        log.debug("CardController--updateTicketInDone--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/main")
    public ResponseEntity<CardDto> updateTicket(@RequestBody @Valid UpdateCardRequest payload) {
        log.debug("CardController--updateTicket--IN");
        log.debug("CardController--updateTicket--payload: {}", payload);

        final CardDto card = cardService.updateCard(payload);

        log.debug("CardController--updateTicket--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/assign")
    public ResponseEntity<CardDto> assignTicket(@RequestBody @Valid AssignCardRequest payload) {
        log.debug("CardController--assignTicket--IN");
        log.debug("CardController--assignTicket--payload: {}", payload);

        final CardDto card = cardService.assignCard(payload);

        log.debug("CardController--assignTicket--OUT");
        return ResponseEntity.ok().body(card);
    }

    @PutMapping("/unassign/{id}")
    public ResponseEntity<CardDto> unassignTicket(@PathVariable Long id) {
        log.debug("CardController--unassignTicket--IN");
        log.debug("CardController--unassignTicket--id: {}", id);

        final CardDto card = cardService.unassignCard(id);

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
    public ResponseEntity<CardDto> addCardComment(@RequestBody @Valid AddCardCommentRequest payload) {
        log.debug("CardController--addCardComment--IN");

        final CardDto card = cardService.addCommentToCard(payload);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/v1/comment").toUriString());
        log.debug("CardController--addCardComment--uri: {}", uri);

        log.debug("CardController--addCardComment--OUT");
        return ResponseEntity.created(uri).body(card);
    }
}
