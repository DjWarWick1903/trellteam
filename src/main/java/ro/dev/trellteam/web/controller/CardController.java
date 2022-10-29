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
import ro.dev.trellteam.web.response.ObjectResponse;
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
    public ResponseEntity<ObjectResponse> createTicket(@RequestBody @Valid CreateCardRequest payload) {
        log.debug("CardController--createTicket--IN");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/v1/main").toUriString());
        log.debug("CardController--createTicket--uri: {}", uri);

        final CardDto card = cardService.createTicket(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<ObjectResponse> getTicketById(@PathVariable Long id) {
        log.debug("CardController--getTicketById--IN");
        if(id == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final CardDto card = cardMapper.domainToDto(cardService.findById(id));
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/main/todo")
    public ResponseEntity<ObjectResponse> updateTicketInToDo(@RequestBody @Valid UpdateCardStatusRequest payload) {
        log.debug("CardController--updateTicketInToDo--IN");
        final CardDto card = cardService.updateCardStatusInToDo(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/main/progress")
    public ResponseEntity<ObjectResponse> updateTicketInProgress(@RequestBody @Valid UpdateCardStatusRequest payload) {
        log.debug("CardController--updateTicketInProgress--IN");
        final CardDto card = cardService.updateCardStatusInProgress(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/main/done")
    public ResponseEntity<ObjectResponse> updateTicketInDone(@RequestBody @Valid UpdateCardStatusRequest payload) {
        log.debug("CardController--updateTicketInDone--IN");
        final CardDto card = cardService.updateCardStatusInDone(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/main")
    public ResponseEntity<ObjectResponse> updateTicket(@RequestBody @Valid UpdateCardRequest payload) {
        log.debug("CardController--updateTicket--IN");
        final CardDto card = cardService.updateCard(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/assign")
    public ResponseEntity<ObjectResponse> assignTicket(@RequestBody @Valid AssignCardRequest payload) {
        log.debug("CardController--assignTicket--IN");
        final CardDto card = cardService.assignCard(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/unassign/{id}")
    public ResponseEntity<ObjectResponse> unassignTicket(@PathVariable Long id) {
        log.debug("CardController--unassignTicket--IN");
        if(id == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final CardDto card = cardService.unassignCard(id);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/comment/{idCard}")
    public ResponseEntity<ObjectResponse> getComments(@PathVariable Long idCard) {
        log.debug("CardController--getComments--IN");
        if(idCard == null) {
            throw new TrellGenericException("TRELL_ERR_8");
        }

        final CardDto card = cardMapper.domainToDto(cardService.findById(idCard));
        final ObjectResponse response = new ObjectResponse(card.getComments());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/comment")
    public ResponseEntity<ObjectResponse> addCardComment(@RequestBody @Valid AddCardCommentRequest payload) {
        log.debug("CardController--addCardComment--IN");

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/card/v1/comment").toUriString());
        log.debug("CardController--addCardComment--uri: {}", uri);

        final CardDto card = cardService.addCommentToCard(payload);
        final ObjectResponse response = new ObjectResponse(card);
        return ResponseEntity.created(uri).body(response);
    }
}
