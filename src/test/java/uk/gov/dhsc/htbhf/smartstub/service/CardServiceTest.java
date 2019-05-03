package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithFirstName;
import static uk.gov.dhsc.htbhf.smartstub.helper.DepositFundsRequestDTOTestDataFactory.aValidDepositFundsRequest;

class CardServiceTest {

    private CardService cardService = new CardService();

    @ParameterizedTest
    @CsvSource({
            "NOTOPUP, 1-",
            "PARTIAL, 2-",
            "BALANCEERROR, 3-",
            "PAYMENTERROR, 4-",
            "MARGE, 9-"

    })
    void shouldSuccessfullyCreateCard(String firstName, String expectedCardIdPrefix) {
        //Given
        CardRequestDTO cardRequestDTO = aCardRequestWithFirstName(firstName);
        //When
        CreateCardResponse response = cardService.createCard(cardRequestDTO);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getCardAccountId()).isNotNull().startsWith(expectedCardIdPrefix);
    }

    @Test
    void shouldThrowExceptionWhenErrorFirstNameProvided() {
        //Given
        CardRequestDTO cardRequestDTO = aCardRequestWithFirstName("CardError");
        //When
        RuntimeException thrown = catchThrowableOfType(() -> cardService.createCard(cardRequestDTO), RuntimeException.class);
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
    void shouldDepositFundsSuccessfully() {
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
