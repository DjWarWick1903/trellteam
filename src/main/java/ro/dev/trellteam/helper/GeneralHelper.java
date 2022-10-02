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
     * Method used to convert a date string with a specified format.
     * @param dateString
     * @param dateFormat
     * @return Date
     */
    public static Date convertStringToDate(String dateString, String dateFormat) {
        log.debug("GeneralHelper--convertStringToDate--IN");
        log.debug("GeneralHelper--convertStringToDate--dateString: {}", dateString);
        log.debug("GeneralHelper--convertStringToDate--dateFormat: {}", dateFormat);

        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (final Exception e) {
            log.error(e.getMessage());
        }

        log.debug("GeneralHelper--convertStringToDate--IN");
        return date;
    }

    /**
     * Method used to extract Organisation object from a Map with String values.
     * @param organisationData
     * @return Organisation
     */
    public static Organisation getOrganisationFromMap(final Map<String, String> organisationData) {
        log.debug("GeneralHelper--getOrganisationFromMap--IN");
        Organisation organisation = new Organisation();
        organisation.setName(organisationData.get("name"));
        organisation.setCUI(organisationData.get("cui"));
        organisation.setDomain(organisationData.get("domain"));
        organisation.setSign(organisationData.get("sign"));
        organisation.setDateCreated(new Date());

        log.debug("GeneralHelper--getOrganisationFromMap--name: {}", organisation.getName());
        log.debug("GeneralHelper--getOrganisationFromMap--cui: {}", organisation.getCUI());
        log.debug("GeneralHelper--getOrganisationFromMap--domain: {}", organisation.getDomain());
        log.debug("GeneralHelper--getOrganisationFromMap--sign: {}", organisation.getSign());

        log.debug("GeneralHelper--getOrganisationFromMap--OUT");
        return organisation;
    }

    /**
     * Method used to extract Employee object from a Map with String values.
     * @param employeeData
     * @return Employee
     */
    public static Employee getEmployeeFromMap(final Map<String, String> employeeData) throws Exception {
        log.debug("GeneralHelper--getEmployeeFromMap--IN");
        Employee employee = new Employee();
        employee.setCNP(employeeData.get("cnp"));
        employee.setFirstName(employeeData.get("firstName"));
        employee.setLastName(employeeData.get("lastName"));
        employee.setPhone(employeeData.get("phone"));

        final String bdayString = employeeData.get("bday");
        final Date bday = new SimpleDateFormat("yyyy-MM-dd").parse(bdayString);
        employee.setBday(bday);

        log.debug("GeneralHelper--getEmployeeFromMap--firstName: {}", employee.getFirstName());
        log.debug("GeneralHelper--getEmployeeFromMap--lastName: {}", employee.getLastName());
        log.debug("GeneralHelper--getEmployeeFromMap--phone: {}", employee.getPhone());
        log.debug("GeneralHelper--getEmployeeFromMap--cnp: {}", employee.getCNP());
        log.debug("GeneralHelper--getEmployeeFromMap--bday: {}", employee.getBday());

        log.debug("GeneralHelper--getEmployeeFromMap--OUT");
        return employee;
    }

    /**
     * Method used to extract Account object from a Map with String values.
     * @param accountData
     * @return Account
     */
    public static Account getAccountFromMap(final Map<String, String> accountData) {
        log.debug("GeneralHelper--getAccountFromMap--IN");
        Account account = new Account();
        account.setEmail(accountData.get("email"));
        account.setUsername(accountData.get("username"));
        account.setPassword(accountData.get("password"));
        account.setDisabled(0);
        account.setDateCreated(new Date());

        log.debug("GeneralHelper--getAccountFromMap--email: {}", account.getEmail());
        log.debug("GeneralHelper--getAccountFromMap--username: {}", account.getUsername());
        log.debug("GeneralHelper--getAccountFromMap--password: {}", account.getPassword());
        log.debug("GeneralHelper--getAccountFromMap--OUT");
        return account;
    }

    /**
     * Method used to return a string describing what was changed in a card.
     * @param oldCard
     * @param newCard
     * @param changed
     * @return String
     */
    public static String getCardLogText(final Card oldCard, final Card newCard, final String changed) {
        log.debug("GeneralHelper--getCardLogText--IN");
        log.debug("GeneralHelper--getCardLogText--changed: {}", changed);

        String logText = null;
        switch (changed) {
            case "title":
                logText = "Title: " + oldCard.getTitle() + " ---> " + newCard.getTitle();
                break;
            case "type":
                logText = "Type: " + oldCard.getType().getName() + " ---> " + newCard.getType().getName();
                break;
            case "urgency":
                logText = "Urgency: " + oldCard.getUrgency() + " ---> " + newCard.getUrgency();
                break;
            case "difficulty":
                logText = "Difficulty: " + oldCard.getDifficulty() + " ---> " + newCard.getDifficulty();
                break;
            case "notes":
                logText = "Notes: " + oldCard.getNotes() + " ---> " + newCard.getNotes();
                break;
            case "description":
                logText = "Description: " + oldCard.getDescription() + " ---> " + newCard.getDescription();
        }

        log.debug("GeneralHelper--getCardLogText--logText: {}", logText);
        log.debug("GeneralHelper--getCardLogText--OUT");
        return logText;
    }
}
