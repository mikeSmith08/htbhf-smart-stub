package uk.gov.dhsc.htbhf.smartstub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import java.util.UUID;
import javax.validation.Valid;

import static uk.gov.dhsc.htbhf.smartstub.service.CardIdBuilder.buildCardIdForFirstName;

/**
 * This service is responsible for creating stub responses to card requests. The current stub responses are:
 * See README.md for details on mappings.
 */
@Service
@Slf4j
public class CardService {

    public CreateCardResponse createCard(CardRequestDTO cardRequestDTO) {
        String firstName = cardRequestDTO.getFirstName();
        if (FirstNameScenario.CARD_ERROR.matchesFirstName(firstName)) {
            String message = "First name provided (" + firstName + ") has been configured to trigger an Exception when creating a card";
            log.info(message);
            throw new RuntimeException(message);
        }
        return CreateCardResponse.builder()
                .cardAccountId(buildCardIdForFirstName(cardRequestDTO.getFirstName()))
                .build();
    }

    public CardBalanceResponse getCardBalance(String cardId) {
        return CardBalanceResponse.builder()
                .availableBalanceInPence(0)
                .ledgerBalanceInPence(0)
                .build();
    }

    public DepositFundsResponse depositFunds(@PathVariable("cardId") String cardId,
                                             @RequestBody @Valid DepositFundsRequestDTO depositFundsRequestDTO) {
        return DepositFundsResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .build();
    }

}
