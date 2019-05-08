package uk.gov.dhsc.htbhf.smartstub.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import java.util.Optional;
import java.util.UUID;

import static uk.gov.dhsc.htbhf.smartstub.service.CardIdBuilder.buildCardIdForFirstName;
import static uk.gov.dhsc.htbhf.smartstub.service.FirstNameScenario.PAYMENT_ERROR;
import static uk.gov.dhsc.htbhf.smartstub.service.FirstNameScenario.findScenarioMatchingCardIdPrefix;

/**
 * This service is responsible for creating stub responses to card requests. The current stub responses are:
 * See README.md for details on mappings.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CardService {

    private final CardBalanceGenerator cardBalanceGenerator;

    public CreateCardResponse createCard(CardRequestDTO cardRequestDTO) {
        String firstName = cardRequestDTO.getFirstName();
        if (FirstNameScenario.CARD_ERROR.matchesFirstName(firstName)) {
            String message = String.format("First name provided [%s] has been configured to trigger an Exception when creating a card", firstName);
            log.info(message);
            throw new RuntimeException(message);
        }
        return CreateCardResponse.builder()
                .cardAccountId(buildCardIdForFirstName(cardRequestDTO.getFirstName()))
                .build();
    }

    public CardBalanceResponse getCardBalance(String cardId) {
        int balance = cardBalanceGenerator.generateBalanceForCardId(cardId);
        return CardBalanceResponse.builder()
                .availableBalanceInPence(balance)
                .ledgerBalanceInPence(balance)
                .build();
    }

    public DepositFundsResponse depositFunds(String cardId, DepositFundsRequestDTO depositFundsRequestDTO) {
        if (isPaymentErrorScenario(cardId)) {
            String message = String.format("Card ID provided [%s] matches prefix [%s] that has been configured to trigger an Exception when trying to deposit funds to the card",
                    cardId, PAYMENT_ERROR.getCardIdPrefixToMatch());
            log.info(message);
            throw new RuntimeException(message);
        }
        return DepositFundsResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .build();
    }

    private boolean isPaymentErrorScenario(String cardId) {
        Optional<FirstNameScenario> scenarioMatchingCardIdPrefix = findScenarioMatchingCardIdPrefix(cardId);
        return scenarioMatchingCardIdPrefix.isPresent() && scenarioMatchingCardIdPrefix.get() == PAYMENT_ERROR;
    }

}
