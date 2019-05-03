package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
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

    @ParameterizedTest
    @CsvSource({
            "1, NO_TOP_UP",
            "2, PARTIAL",
            "3, BALANCE_ERROR",
            "4, PAYMENT_ERROR"
    })
    void shouldMatchCardPrefix(Integer prefix, FirstName firstName) {
        String cardId = prefix + "-sdfk4-sdfjlkn";
        boolean matches = firstName.matchesCardIdPrefix(cardId);
        assertThat(matches).isTrue();
    }

    @Test
    void shouldNotMatchCardPrefixForCardErrorWithNoPrefixDefined() {
        String cardId = "2-sdfk4-sdfjlkn";
        boolean matches = FirstName.CARD_ERROR.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldFailToMatchCardPrefixWhenNoPrefix(FirstName firstName) {
        String cardId = "sdfk4sdfjlkn";
        boolean matches = firstName.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldFailToMatchCardPrefixWithUndefinedPrefix(FirstName firstName) {
        String cardId = "8-sdfk4sdfjlkn";
        boolean matches = firstName.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldFailToMatchCardPrefixWhenBlankCardId(FirstName firstName) {
        String cardId = "";
        boolean matches = firstName.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldFailToMatchCardPrefixWhenNoCardId(FirstName firstName) {
        String cardId = null;
        boolean matches = firstName.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstName.class)
    void shouldFailToMatchCardPrefixWhenNotPrefixedWithInt(FirstName firstName) {
        String cardId = "sdf-skfdj-skdfj";
        boolean matches = firstName.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }
}
