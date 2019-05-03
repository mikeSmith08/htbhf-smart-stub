package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(firstName.matchesFirstName(matchingFirstName)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldNotMatchCardRequest(FirstName firstName) {
        assertThat(firstName.matchesFirstName("Homer")).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldNotMatchCardRequestWithNoFirstName(FirstName firstName) {
        assertThat(firstName.matchesFirstName(null)).isFalse();
    }
}
