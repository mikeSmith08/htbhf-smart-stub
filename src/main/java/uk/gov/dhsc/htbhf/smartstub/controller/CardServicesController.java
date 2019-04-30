package uk.gov.dhsc.htbhf.smartstub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import java.util.UUID;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cards")
@Slf4j
public class CardServicesController {

    @PostMapping
    public CreateCardResponse createCard(CardRequestDTO cardRequestDTO) {
        log.debug("Received create card request: {}", cardRequestDTO);
        CreateCardResponse createCardResponse = CreateCardResponse.builder()
                .cardAccountId(UUID.randomUUID().toString())
                .build();
        log.debug("Returning create card response: {}", createCardResponse);
        return createCardResponse;
    }

    @GetMapping(path = "/{cardId}/balance")
    public CardBalanceResponse getCardBalance(@PathVariable("cardId") String cardId) {
        log.debug("Received get card balance request for cardId: {}", cardId);
        CardBalanceResponse cardBalanceResponse = CardBalanceResponse.builder()
                .availableBalanceInPence(1000)
                .ledgerBalanceInPence(500)
                .build();
        log.debug("Returning card balance: {}", cardBalanceResponse);
        return cardBalanceResponse;
    }

    @PostMapping("/{cardId}/deposit")
    public DepositFundsResponse depositFunds(@PathVariable("cardId") String cardId,
                                             @RequestBody @Valid DepositFundsRequestDTO depositFundsRequestDTO) {
        log.debug("Received deposit funds request for cardId: {}, {}", cardId, depositFundsRequestDTO);
        DepositFundsResponse response = DepositFundsResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .build();
        log.debug("Returning deposit funds response: {}", response);
        return response;
    }

}
