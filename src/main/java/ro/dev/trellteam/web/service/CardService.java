package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.domain.Card;
import ro.dev.trellteam.domain.CardLog;
import ro.dev.trellteam.domain.Comment;
import ro.dev.trellteam.domain.Type;
import ro.dev.trellteam.enums.CardStatusEnum;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.helper.GeneralHelper;
import ro.dev.trellteam.web.dto.CardDto;
import ro.dev.trellteam.web.mapper.AccountMapperImpl;
import ro.dev.trellteam.web.mapper.CardMapperImpl;
import ro.dev.trellteam.web.mapper.TypeMapperImpl;
import ro.dev.trellteam.web.repository.AccountRepository;
import ro.dev.trellteam.web.repository.BoardRepository;
import ro.dev.trellteam.web.repository.CardRepository;
import ro.dev.trellteam.web.repository.TypeRepository;
import ro.dev.trellteam.web.request.card.AddCardCommentRequest;
import ro.dev.trellteam.web.request.card.AssignCardRequest;
import ro.dev.trellteam.web.request.card.CreateCardRequest;
import ro.dev.trellteam.web.request.card.UpdateCardRequest;
import ro.dev.trellteam.web.request.card.UpdateCardStatusRequest;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CardService {

    // Service
    private final TransactionalOperations transactionalOperations;

    // Repository
    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final TypeRepository typeRepository;
    private final AccountRepository accountRepository;

    // Mappers
    private final CardMapperImpl cardMapper;
    private final AccountMapperImpl accountMapper;
    private final TypeMapperImpl typeMapper;

    public CardDto createTicket(final CreateCardRequest request) {
        log.debug("CardService::createTicket: {}", request);

        final String username = request.getCard().getPublisher().getUsername();

        final Account publisher = accountRepository.findByUsername(username);
        final Type type = typeRepository.findById(request.getTypeId()).get();

        if(type == null) {
            throw new TrellGenericException("TRELL_ERR_1");
        }

        Card card = cardMapper.dtoToDomain(request.getCard());
        card.setPublisher(publisher);
        card.setType(type);
        card.setStatus(CardStatusEnum.TO_DO);

        final Board board = boardRepository.findById(request.getBoardId()).get();

        CardLog cardLog = new CardLog();
        cardLog.setUser(publisher);
        cardLog.setLogDate(new Date());
        cardLog.setText("Ticket created");

        card = transactionalOperations.createCard(board, card, cardLog);

        return cardMapper.domainToDto(card);
    }

    public CardDto updateCardStatusInToDo(final UpdateCardStatusRequest request) {
        log.debug("CardService::updateCardStatusInToDo: {}", request);
        Card card = cardRepository.findById(request.getCardId()).get();
        if(card == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        final Account account = accountRepository.findByUsername(request.getUsername());
        if(account == null) {
            throw new TrellGenericException("TRELL_ERR_3");
        }

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(account);
        cardLog.setText("Status: " + card.getStatus() + " ---> TO DO");

        card = transactionalOperations.changeCardStatus(card, cardLog, CardStatusEnum.TO_DO);
        return cardMapper.domainToDto(card);
    }

    public CardDto updateCardStatusInProgress(final UpdateCardStatusRequest request) {
        log.debug("CardService::updateCardStatusInProgress: {}", request);
        Card card = cardRepository.findById(request.getCardId()).get();
        if(card == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        final Account account = accountRepository.findByUsername(request.getUsername());
        if(account == null) {
            throw new TrellGenericException("TRELL_ERR_3");
        }

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(account);
        cardLog.setText("Status: " + card.getStatus() + " ---> IN PROGRESS");

        card = transactionalOperations.changeCardStatus(card, cardLog, CardStatusEnum.IN_PROGRESS);
        return cardMapper.domainToDto(card);
    }

    public CardDto updateCardStatusInDone(final UpdateCardStatusRequest request) {
        log.debug("CardService::updateCardStatusInDone: {}", request);
        Card card = cardRepository.findById(request.getCardId()).get();
        if(card == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        final Account account = accountRepository.findByUsername(request.getUsername());
        if(account == null) {
            throw new TrellGenericException("TRELL_ERR_3");
        }

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(account);
        cardLog.setText("Status: " + card.getStatus() + " ---> DONE");

        card = transactionalOperations.changeCardStatus(card, cardLog, CardStatusEnum.DONE);
        return cardMapper.domainToDto(card);
    }

    public CardDto updateCard(final UpdateCardRequest request) {
        log.debug("CardService::updateCard: {}", request);
        Card oldCard = cardRepository.findById(request.getCardId()).get();
        if(oldCard == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        // new object with which we compare the old one and get the log
        Card newCard = new Card();
        newCard.setTitle(request.getTitle());
        final Type type = typeRepository.findById(request.getTypeId()).get();
        if(type == null) {
            throw new TrellGenericException("TRELL_ERR_1");
        }
        newCard.setType(type);
        newCard.setUrgency(request.getUrgency());
        newCard.setDifficulty(request.getDifficulty());
        newCard.setDescription(request.getDescription());
        newCard.setNotes(request.getNotes());

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(accountRepository.findByUsername(request.getUsername()));
        cardLog.setText(GeneralHelper.getCardLogText(oldCard, newCard, request.getChanged()));

        oldCard = transactionalOperations.updateCard(oldCard, cardLog);

        return cardMapper.domainToDto(oldCard);
    }

    public CardDto assignCard(final AssignCardRequest request) {
        Card card = cardRepository.findById(request.getCardId()).get();
        if(card == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        final Account newAsignee = accountRepository.findByUsername(request.getUsername());
        if(newAsignee == null) {
            throw new TrellGenericException("TRELL_ERR_3");
        }

        final Account oldAsignee = card.getAssigned();
        final String pastUsername = oldAsignee == null ? "Undefined" : oldAsignee.getUsername();

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(newAsignee);
        cardLog.setText("Assigned: " + pastUsername + " ---> " + request.getUsername());

        card.setAssigned(newAsignee);
        card = transactionalOperations.assignCard(card, cardLog);

        return cardMapper.domainToDto(card);
    }

    public CardDto unassignCard(final Long id) {
        Card card = cardRepository.findById(id).get();
        if(card == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        CardLog cardLog = new CardLog();
        cardLog.setLogDate(new Date());
        cardLog.setUser(card.getAssigned());
        cardLog.setText("Assigned: " + card.getAssigned().getUsername() + " ---> Undefined");

        card.setAssigned(null);
        card = transactionalOperations.unassignCard(card, cardLog);

        return cardMapper.domainToDto(card);
    }

    public CardDto addCommentToCard(final AddCardCommentRequest request) {
        final Account user = accountRepository.findByUsername(request.getUsername());
        if(user == null) {
            throw new TrellGenericException("TRELL_ERR_3");
        }

        Card card = cardRepository.findById(request.getCardId()).get();
        if(card == null) {
            throw new TrellGenericException("TRELL_ERR_2");
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCommentDate(new Date());
        comment.setText(request.getComment());

        CardLog cardLog = new CardLog();
        cardLog.setUser(user);
        cardLog.setLogDate(new Date());
        cardLog.setText("User " + user.getUsername() + " added comment: " + request.getComment());

        card = transactionalOperations.createCardComment(card, comment, cardLog);

        return cardMapper.domainToDto(card);
    }

    /**
     * Method used to save a card in the database.
     * @param card
     * @return Card
     */
    public Card save(Card card) {
        log.debug("CardService--createCard--IN");

        card = cardRepository.save(card);

        log.debug("CardService--createCard--card: {}", card);
        log.debug("CardService--createCard--OUT");
        return card;
    }

    /**
     * Method used to get a card by it's id.
     * @param id
     * @return Card
     */
    public Card findById(final Long id) {
        log.debug("CardService--findById--IN");

        final Card card = cardRepository.findById(id).get();

        log.debug("CardService--findById--card: {}", card);
        log.debug("CardService--findById--OUT");
        return card;
    }

    /**
     * Method used to get a list of cards that were published by an employee.
     * @param publisher
     * @return List
     */
    public List<Card> getAllPublisherCards(final Account publisher) {
        log.debug("CardService--getAllPublisherCards--IN");
        log.debug("CardService--getAllPublisherCards--publisher id: {}", publisher.getId());

        final List<Card> cards = cardRepository.findAllByPublisher(publisher);

        log.debug("CardService--getAllPublisherCards--card count: {}", cards.size());
        log.debug("CardService--getAllPublisherCards--OUT");
        return cards;
    }

    /**
     * Method used to get a list of cards on which an employee worked on.
     * @param assigned
     * @return List
     */
    public List<Card> getAllAssigneeCards(final Account assigned) {
        log.debug("CardService--getAllAssigneeCards--IN");
        log.debug("CardService--getAllAssigneeCards--assignee id: {}", assigned.getId());

        final List<Card> cards = cardRepository.findAllByAssigned(assigned);

        log.debug("CardService--getAllAssigneeCards--card count: {}", cards.size());
        log.debug("CardService--getAllAssigneeCards--OUT");
        return cards;
    }
}
