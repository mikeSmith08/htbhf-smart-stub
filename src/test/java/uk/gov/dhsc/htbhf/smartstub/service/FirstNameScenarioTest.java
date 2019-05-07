package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FirstNameScenarioTest {

    @ParameterizedTest
    @CsvSource({
            "balanceerror, BALANCE_ERROR",
            "carderror, CARD_ERROR",
            "notopup, NO_TOP_UP",
            "partial, PARTIAL",
            "paymenterror, PAYMENT_ERROR"
    })
    void shouldMatchScenarioToFirstName(String matchingFirstName, FirstNameScenario firstNameScenario) {
        assertThat(firstNameScenario.matchesFirstName(matchingFirstName)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldNotMatchCardRequest(FirstNameScenario firstNameScenario) {
        assertThat(firstNameScenario.matchesFirstName("Homer")).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldNotMatchCardRequestWithNoFirstName(FirstNameScenario firstNameScenario) {
        assertThat(firstNameScenario.matchesFirstName(null)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "1, NO_TOP_UP",
            "2, PARTIAL",
            "3, BALANCE_ERROR",
            "4, PAYMENT_ERROR"
    })
    void shouldMatchCardPrefix(Integer prefix, FirstNameScenario firstNameScenario) {
        String cardId = prefix + "-sdfk4-sdfjlkn";
        boolean matches = firstNameScenario.matchesCardIdPrefix(cardId);
        assertThat(matches).isTrue();
    }

    @Test
    void shouldNotMatchCardPrefixForCardErrorWithNoPrefixDefined() {
        String cardId = "2-sdfk4-sdfjlkn";
        boolean matches = FirstNameScenario.CARD_ERROR.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldFailToMatchScenarioWhenNoCardIdPrefix(FirstNameScenario firstNameScenario) {
        String cardId = "sdfk4sdfjlkn";
        boolean matches = firstNameScenario.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldFailToMatchScenarioWithUndefinedCardIdPrefix(FirstNameScenario firstNameScenario) {
        String cardId = "8-sdfk4sdfjlkn";
        boolean matches = firstNameScenario.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldFailToMatchScenarioWithBlankCardId(FirstNameScenario firstNameScenario) {
        String cardId = "";
        boolean matches = firstNameScenario.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldFailToMatchScenarioWithNoCardId(FirstNameScenario firstNameScenario) {
        String cardId = null;
        boolean matches = firstNameScenario.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @EnumSource(FirstNameScenario.class)
    void shouldFailToMatchScenarioWhenCardIdNotPrefixedWithInt(FirstNameScenario firstNameScenario) {
        String cardId = "sdf-skfdj-skdfj";
        boolean matches = firstNameScenario.matchesCardIdPrefix(cardId);
        assertThat(matches).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "1-sdf-skfdj-skdfj, NO_TOP_UP",
            "2-sdf-skfdj-skdfj, PARTIAL",
            "3-sdf-skfdj-skdfj, BALANCE_ERROR",
            "4-sdf-skfdj-skdfj, PAYMENT_ERROR"
    })
    void shouldMatchCardIdWithPrefixToCorrectEnum(String cardId, FirstNameScenario expectedFirstNameScenario) {
        Optional<FirstNameScenario> foundFirstName = FirstNameScenario.findScenarioMatchingCardIdPrefix(cardId);
        assertThat(foundFirstName)
                .isPresent()
                .contains(expectedFirstNameScenario);
    }

    @ParameterizedTest
    @CsvSource({
            "carderror, CARD_ERROR",
            "notopup, NO_TOP_UP",
            "partial, PARTIAL",
            "balanceerror, BALANCE_ERROR",
            "paymenterror, PAYMENT_ERROR"
    })
    void shouldMatchFirstNameToCorrectEnum(String firstName, FirstNameScenario expectedFirstNameScenario) {
        Optional<FirstNameScenario> foundFirstName = FirstNameScenario.findScenarioMatchingCardRequestFirstName(firstName);
        assertThat(foundFirstName)
                .isPresent()
                .contains(expectedFirstNameScenario);
    }
}
