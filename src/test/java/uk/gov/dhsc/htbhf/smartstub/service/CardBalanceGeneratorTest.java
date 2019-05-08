package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@SpringBootTest
class CardBalanceGeneratorTest {

    @Autowired
    private CardBalanceGenerator cardBalanceGenerator;

    @ParameterizedTest
    @ValueSource(strings = {
            "5-sadkfjh",
            "6-sadkfjh",
            "7-sadkfjh",
            "8-sadkfjh",
            "sadkfjh",
            ""
    })
    void shouldGenerateLowBalance(String cardId) {
        //When
        int balance = cardBalanceGenerator.generateBalanceForCardId(cardId);
        //Then
        assertThat(balance).isBetween(0, 1239);
    }

    @Test
    void shouldGeneratePartialBalanceForNullCardId() {
        //When
        int balance = cardBalanceGenerator.generateBalanceForCardId(null);
        //Then
        assertThat(balance).isBetween(0, 1239);
    }

    @Test
    void shouldGeneratePartialBalanceForCardIdStartingWith1() {
        //Given
        String cardId = "1-sadkfjh";
        //When
        int balance = cardBalanceGenerator.generateBalanceForCardId(cardId);
        //Then
        assertThat(balance).isEqualTo(100000);
    }

    @Test
    void shouldGeneratePartialBalanceForCardIdStartingWith2() {
        //Given
        String cardId = "2-sadkfjh";
        //When
        int balance = cardBalanceGenerator.generateBalanceForCardId(cardId);
        //Then
        assertThat(balance).isEqualTo(1860);
    }

    @Test
    void shouldThrowExceptionForBalanceErrorScenarioForCardIdStartingWith3() {
        //Given
        String cardId = "3-sadkfjh";
        //When
        RuntimeException thrown = catchThrowableOfType(() -> cardBalanceGenerator.generateBalanceForCardId(cardId), RuntimeException.class);
        //Then
        assertThat(thrown).hasMessage("Card ID provided [3-sadkfjh] matches prefix [3] that has been configured to trigger an Exception when getting the balance for the card");
    }

    @Test
    void shouldGenerateLowBalanceForPaymentErrorScenarioWithCardIdStartingWith4() {
        //Given
        String cardId = "4-sadkfjh";
        //When
        int balance = cardBalanceGenerator.generateBalanceForCardId(cardId);
        //Then
        assertThat(balance).isBetween(0, 1239);
    }
}
