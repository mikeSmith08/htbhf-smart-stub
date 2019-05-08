package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithFirstName;
import static uk.gov.dhsc.htbhf.smartstub.helper.DepositFundsRequestDTOTestDataFactory.aValidDepositFundsRequest;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardBalanceGenerator cardBalanceGenerator;

    @InjectMocks
    private CardService cardService;

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
        verifyZeroInteractions(cardBalanceGenerator);
    }

    @Test
    void shouldThrowExceptionWhenErrorFirstNameProvided() {
        //Given
        CardRequestDTO cardRequestDTO = aCardRequestWithFirstName("CardError");
        //When
        RuntimeException thrown = catchThrowableOfType(() -> cardService.createCard(cardRequestDTO), RuntimeException.class);
        //Then
        assertThat(thrown).hasMessage("First name provided [CardError] has been configured to trigger an Exception when creating a card");
        verifyZeroInteractions(cardBalanceGenerator);
    }

    @Test
    void shouldSuccessfullyGetCardBalance() {
        //Given
        String cardId = "myId";
        given(cardBalanceGenerator.generateBalanceForCardId(anyString())).willReturn(52);
        //When
        CardBalanceResponse response = cardService.getCardBalance(cardId);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getAvailableBalanceInPence()).isEqualTo(52);
        assertThat(response.getLedgerBalanceInPence()).isEqualTo(52);
        verify(cardBalanceGenerator).generateBalanceForCardId(cardId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "myId",
            "1-sdlfj",
            "2-dfsdfjn",
            "3-sdfsdfjn",
            "9-asdkkl",
            ""
    })
    void shouldDepositFundsSuccessfully(String cardId) {
        //Given
        DepositFundsRequestDTO requestDTO = aValidDepositFundsRequest();
        //When
        DepositFundsResponse response = cardService.depositFunds(cardId, requestDTO);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getReferenceId()).isNotNull();
        verifyZeroInteractions(cardBalanceGenerator);
    }

    @Test
    void shouldThrowExceptionWhenDepositFundsReceivesCardIdWithPrefix4() {
        //Given
        String cardId = "4-myId";
        DepositFundsRequestDTO requestDTO = aValidDepositFundsRequest();
        //When
        RuntimeException thrown = catchThrowableOfType(() -> cardService.depositFunds(cardId, requestDTO), RuntimeException.class);
        //Then
        assertThat(thrown).hasMessage("Card ID provided [4-myId] matches prefix [4] that has been configured to trigger an Exception when trying to deposit funds to the card");
        verifyZeroInteractions(cardBalanceGenerator);
    }
}
