package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import uk.gov.dhsc.htbhf.smartstub.model.CardRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithFirstName;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aValidCardRequest;

class FirstNameTest {

    @ParameterizedTest
    @CsvSource({
            "balanceerror, BALANCE_ERROR",
            "carderror, CARD_ERROR",
            "notopup, NO_TOP_UP",
            "partial, PARTIAL",
            "paymenterror, PAYMENT_ERROR"
    })
    void shouldMatchCardRequestWithMatchingFirstName(String matchingFirstName, FirstName firstName) {
        CardRequestDTO matchingCardRequestDTO = aCardRequestWithFirstName(matchingFirstName);
        assertThat(firstName.matches(matchingCardRequestDTO)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldNotMatchCardRequest(FirstName firstName) {
        CardRequestDTO cardRequestDTO = aValidCardRequest();
        assertThat(firstName.matches(cardRequestDTO)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldNotMatchCardRequestWithNoFirstName(FirstName firstName) {
        CardRequestDTO cardRequestDTO = aCardRequestWithFirstName(null);
        assertThat(firstName.matches(cardRequestDTO)).isFalse();
    }
}
