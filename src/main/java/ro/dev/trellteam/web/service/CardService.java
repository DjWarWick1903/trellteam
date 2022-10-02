package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Card;
import ro.dev.trellteam.web.repository.CardRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CardService {

    private final CardRepository cardRepository;

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
