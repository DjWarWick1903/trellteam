package ro.dev.trellteam.exceptions;

import java.util.HashMap;

/**
 * Class which contains every error code and it's description.
 */
public class ErrorHelper {

    private static HashMap<String, String> errorList;

    static {
        errorList = new HashMap<>();

        errorList.put("TRELL_ERR_0", "An error has been encountered.");
        errorList.put("TRELL_ERR_1", "Card type was not found.");
        errorList.put("TRELL_ERR_2", "Card was not found.");
        errorList.put("TRELL_ERR_3", "Account was not found.");
        errorList.put("TRELL_ERR_4", "Board was not found.");
    }
}
