package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.CardLog;
import ro.dev.trellteam.repository.CardLogRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CardLogService {
    private final CardLogRepository cardLogRepository;

    /**
     * Method used to save a card log in the database.
     * @param comment
     * @return CardLog
     */
    public CardLog save(CardLog cardLog) {
        log.debug("CardLogService--save--IN");

        cardLog = cardLogRepository.save(cardLog);

        log.debug("CardLogService--save--cardLog: {}", cardLog);
        log.debug("CardLogService--save--OUT");
        return cardLog;
    }

    /**
     * Method used to find a card log in the database starting from it's id.
     * @param id
     * @return CardLog
     */
    public CardLog findById(Long id) {
        log.debug("CardLogService--findById--IN");
        log.debug("CardLogService--findById--id: {}", id);

        final CardLog cardLog = cardLogRepository.findById(id).get();

        log.debug("CardLogService--findById--cardLog: {}", cardLog);
        log.debug("CardLogService--findById--OUT");

        return cardLog;
    }
}
