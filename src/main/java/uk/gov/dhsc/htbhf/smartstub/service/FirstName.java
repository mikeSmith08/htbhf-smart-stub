package uk.gov.dhsc.htbhf.smartstub.service;

import org.apache.commons.lang3.StringUtils;
import uk.gov.dhsc.htbhf.smartstub.model.CardRequestDTO;

/**
 * Contains the first names that will trigger a special scenario response from the card service endpoints.
 */
public enum FirstName {

    CARD_ERROR("CardError", null),
    NO_TOP_UP("NoTopup", 1),
    PARTIAL("Partial", 2),
    BALANCE_ERROR("BalanceError", 3),
    PAYMENT_ERROR("PaymentError", 4),
    EXACT_BALANCE_AMOUNT("ExactBalanceAmount", 5);

    private String nameToMatch;
    private Integer cardIdPrefixToMatch;

    FirstName(String nameToMatch, Integer cardIdPrefixToMatch) {
        this.nameToMatch = nameToMatch;
        this.cardIdPrefixToMatch = cardIdPrefixToMatch;
    }

    /**
     * Checks if the first name in the card request matches a special response
     *
     * @param firstName The first name from the card request
     * @return true if the first name matches one of the special cases
     */
    public boolean matchesFirstName(String firstName) {
        return nameToMatch.equalsIgnoreCase(firstName);
    }

    /**
     * Checks if the given cardId has the correct prefix matching this enum
     *
     * @param cardId The cardId to check
     * @return true if the prefix matches
     */
    public boolean matchesCardIdPrefix(String cardId) {
        if (StringUtils.isEmpty(cardId) || !cardId.contains("-")) {
            return false;
        }
        try {
            String cardIdByBeforeDash = StringUtils.substringBefore(cardId, "-");
            Integer foundPrefix = Integer.parseInt(cardIdByBeforeDash);
            return foundPrefix.equals(cardIdPrefixToMatch);
        } catch (NumberFormatException e) {
            //Prefix is not numeric
            return false;
        }
    }

}
