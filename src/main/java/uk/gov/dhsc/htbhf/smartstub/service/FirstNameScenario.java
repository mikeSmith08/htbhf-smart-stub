package uk.gov.dhsc.htbhf.smartstub.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains the first names that will trigger a special scenario response from the card service endpoints.
 */
public enum FirstNameScenario {

    CARD_ERROR("CardError", null),
    NO_TOP_UP("NoTopup", 1),
    PARTIAL("Partial", 2),
    BALANCE_ERROR("BalanceError", 3),
    PAYMENT_ERROR("PaymentError", 4);

    private String nameToMatch;
    private Integer cardIdPrefixToMatch;

    FirstNameScenario(String nameToMatch, Integer cardIdPrefixToMatch) {
        this.nameToMatch = nameToMatch;
        this.cardIdPrefixToMatch = cardIdPrefixToMatch;
    }

    public String getNameToMatch() {
        return nameToMatch;
    }

    public Integer getCardIdPrefixToMatch() {
        return cardIdPrefixToMatch;
    }

    /**
     * Checks if the first name in the card request matches a special response.
     *
     * @param firstName The first name from the card request
     * @return true if the first name matches one of the special cases
     */
    public boolean matchesFirstName(String firstName) {
        return nameToMatch.equalsIgnoreCase(firstName);
    }

    /**
     * Checks if the given cardId has the correct prefix matching this enum.
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

    /**
     * Method used to check if the provided cardId matches any given prefix and return
     * an Optional of that FirstNameScenario or empty.
     *
     * @param cardId The cardId to check for prefixes
     * @return An Optional of the matching FirstNameScenario or empty.
     */
    public static Optional<FirstNameScenario> findScenarioMatchingCardIdPrefix(String cardId) {
        return Stream.of(FirstNameScenario.values())
                .filter(firstNameScenario -> firstNameScenario.matchesCardIdPrefix(cardId))
                .findFirst();
    }

    /**
     * Method used to check if the provided first name in the card request matches any given FirstNameScenario
     * and return an Optional of that FirstNameScenario or empty.
     *
     * @param cardRequestFirstName The first name from the card request
     * @return An Optional of the matching FirstNameScenario or empty.
     */
    public static Optional<FirstNameScenario> findScenarioMatchingCardRequestFirstName(String cardRequestFirstName) {
        return Stream.of(FirstNameScenario.values())
                .filter(firstNameScenario -> firstNameScenario.matchesFirstName(cardRequestFirstName))
                .findFirst();
    }

}
