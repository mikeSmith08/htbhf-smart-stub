package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithFirstName;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aValidCardRequest;
import static uk.gov.dhsc.htbhf.smartstub.helper.DepositFundsRequestDTOTestDataFactory.aValidDepositFundsRequest;

class CardServiceTest {

    private CardService cardService = new CardService();

    @Test
    void shouldSuccessfullyCreateCard() {
        //Given
        CardRequestDTO cardRequestDTO = aValidCardRequest();
        //When
        CreateCardResponse response = cardService.createCard(cardRequestDTO);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getCardAccountId()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenErrorFirstNameProvided() {
        //Given
        CardRequestDTO cardRequestDTO = aCardRequestWithFirstName("CardError");
        //When
        IllegalArgumentException thrown = catchThrowableOfType(() -> cardService.createCard(cardRequestDTO), IllegalArgumentException.class);
        //Then
        assertThat(thrown).hasMessage("First name provided (CardError) has been configured to trigger an Exception when creating a card");
    }

    @Test
    void shouldSuccessfullyGetCardBalance() {
        //Given
        String cardId = "myId";
        //When
        CardBalanceResponse response = cardService.getCardBalance(cardId);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getAvailableBalanceInPence()).isEqualTo(0);
        assertThat(response.getLedgerBalanceInPence()).isEqualTo(0);
    }

    @Test
    void shlouldDepositFundsSuccessfully() {
        //Given
        String cardId = "myId";
        DepositFundsRequestDTO requestDTO = aValidDepositFundsRequest();
        //When
        DepositFundsResponse response = cardService.depositFunds(cardId, requestDTO);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getReferenceId()).isNotNull();
    }
}
