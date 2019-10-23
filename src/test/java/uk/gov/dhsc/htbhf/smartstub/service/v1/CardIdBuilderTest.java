package uk.gov.dhsc.htbhf.smartstub.service.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class CardIdBuilderTest {

    @ParameterizedTest
    @CsvSource({
            "NOTOPUP, 1-",
            "PARTIAL, 2-",
            "BALANCEERROR, 3-",
            "PAYMENTERROR, 4-"

    })
    void shouldBuildCardIdForMatchingFirstName(String firstName, String expectedPrefix) {
        //When
        String cardId = CardIdBuilder.buildCardIdForFirstName(firstName);

        //Then
        assertThat(cardId).startsWith(expectedPrefix);
    }

    @Test
    void shouldBuildCardIdWithDefaultPrefixForUnmatchedName() {
        //Given
        String firstName = "MARGE";

        //When
        String cardId = CardIdBuilder.buildCardIdForFirstName(firstName);

        //Then
        assertThat(cardId).startsWith("9-");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForFirstNameMatchingCardErrorScenario() {
        //Given
        String firstName = "CARDERROR";

        //When
        IllegalArgumentException exception = catchThrowableOfType(() -> CardIdBuilder.buildCardIdForFirstName(firstName), IllegalArgumentException.class);

        //Then
        assertThat(exception).hasMessage("Cannot build card ID for card error scenario");

    }

    @Test
    void shouldBuildCardIdWithDefaultPrefixForNull() {
        //Given
        String firstName = null;

        //When
        String cardId = CardIdBuilder.buildCardIdForFirstName(firstName);

        //Then
        assertThat(cardId).startsWith("9-");
    }
}
