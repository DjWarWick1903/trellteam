package ro.dev.trellteam.helper;

import lombok.extern.slf4j.Slf4j;
import ro.dev.trellteam.domain.Account;
import ro.dev.trellteam.domain.Card;
import ro.dev.trellteam.domain.Employee;
import ro.dev.trellteam.domain.Organisation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public class GeneralHelper {
    /**
     * Method used to return a string describing what was changed in a card.
     * @param oldCard
     * @param newCard
     * @param changed
     * @return String
     */
    public static String getCardLogText(Card oldCard, final Card newCard, final String changed) {
        log.debug("GeneralHelper--getCardLogText--IN");
        log.debug("GeneralHelper--getCardLogText--changed: {}", changed);

        String logText = null;
        switch (changed) {
            case "title":
                logText = "Title: " + oldCard.getTitle() + " ---> " + newCard.getTitle();
                oldCard.setTitle(newCard.getTitle());
                break;
            case "type":
                logText = "Type: " + oldCard.getType().getName() + " ---> " + newCard.getType().getName();
                oldCard.setType(newCard.getType());
                break;
            case "urgency":
                logText = "Urgency: " + oldCard.getUrgency() + " ---> " + newCard.getUrgency();
                oldCard.setUrgency(newCard.getUrgency());
                break;
            case "difficulty":
                logText = "Difficulty: " + oldCard.getDifficulty() + " ---> " + newCard.getDifficulty();
                oldCard.setDifficulty(newCard.getDifficulty());
                break;
            case "notes":
                logText = "Notes: " + oldCard.getNotes() + " ---> " + newCard.getNotes();
                oldCard.setNotes(newCard.getNotes());
                break;
            case "description":
                logText = "Description: " + oldCard.getDescription() + " ---> " + newCard.getDescription();
                oldCard.setDescription(newCard.getDescription());
        }

        log.debug("GeneralHelper--getCardLogText--logText: {}", logText);
        log.debug("GeneralHelper--getCardLogText--OUT");
        return logText;
    }
}
