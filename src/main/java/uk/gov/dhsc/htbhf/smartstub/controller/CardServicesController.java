package uk.gov.dhsc.htbhf.smartstub.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uk.gov.dhsc.htbhf.smartstub.model.*;
import uk.gov.dhsc.htbhf.smartstub.service.CardService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cards")
@AllArgsConstructor
@Slf4j
public class CardServicesController {

    private CardService cardService;

    @PostMapping
    public CreateCardResponse createCard(@RequestBody @Valid CardRequestDTO cardRequestDTO) {
        log.debug("Received create card request: {}", cardRequestDTO);
        CreateCardResponse createCardResponse = cardService.createCard(cardRequestDTO);
        log.debug("Returning create card response: {}", createCardResponse);
        return createCardResponse;
    }

    @GetMapping(path = "/{cardId}/balance")
    public CardBalanceResponse getCardBalance(@PathVariable("cardId") String cardId) {
        log.debug("Received get card balance request for cardId: {}", cardId);
        CardBalanceResponse cardBalanceResponse = cardService.getCardBalance(cardId);
        log.debug("Returning card balance: {}", cardBalanceResponse);
        return cardBalanceResponse;
    }

    @PostMapping("/{cardId}/deposit")
    public DepositFundsResponse depositFunds(@PathVariable("cardId") String cardId,
                                             @RequestBody @Valid DepositFundsRequestDTO depositFundsRequestDTO) {
        log.debug("Received deposit funds request for cardId: {}, {}", cardId, depositFundsRequestDTO);
        DepositFundsResponse response = cardService.depositFunds(cardId, depositFundsRequestDTO);
        log.debug("Returning deposit funds response: {}", response);
        return response;
    }

}
