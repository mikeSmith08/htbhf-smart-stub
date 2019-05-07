package uk.gov.dhsc.htbhf.smartstub.service;

import java.util.Optional;
import java.util.UUID;

import static uk.gov.dhsc.htbhf.smartstub.service.FirstNameScenario.CARD_ERROR;
import static uk.gov.dhsc.htbhf.smartstub.service.FirstNameScenario.findScenarioMatchingCardRequestFirstName;

public class CardIdBuilder {

    private static final int DEFAULT_CARD_ID_PREFIX = 9;

    public static String buildCardIdForFirstName(String firstName) {
        Optional<FirstNameScenario> optionalFirstNameScenario = findScenarioMatchingCardRequestFirstName(firstName);
        if (optionalFirstNameScenario.isPresent()) {
            FirstNameScenario firstNameScenario = optionalFirstNameScenario.get();
            if (firstNameScenario == CARD_ERROR) {
                throw new IllegalArgumentException("Cannot build card ID for card error scenario");
            }
            return buildCardIdForScenario(firstNameScenario);
        }
        return buildDefaultCardId();
    }

    private static String buildDefaultCardId() {
        return buildCardIdWithPrefix(DEFAULT_CARD_ID_PREFIX);
    }

    private static String buildCardIdForScenario(FirstNameScenario firstNameScenario) {
        return buildCardIdWithPrefix(firstNameScenario.getCardIdPrefixToMatch());
    }

    private static String buildCardIdWithPrefix(Integer prefix) {
        return prefix + "-" + UUID.randomUUID().toString();
    }
}
