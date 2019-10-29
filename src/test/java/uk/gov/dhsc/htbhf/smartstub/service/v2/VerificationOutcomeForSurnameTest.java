package uk.gov.dhsc.htbhf.smartstub.service.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dhsc.htbhf.smartstub.model.v2.VerificationOutcome;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.smartstub.service.v2.IdentityAndEligibilityService.*;

class VerificationOutcomeForSurnameTest {

    @ParameterizedTest(name = "Surname={0}, Mobile outcome={1}, Email outcome={2}")
    @MethodSource("verificationOutcomeArguments")
    void shouldGetVerificationOutcomesForSpecifiedSurname(String surname,
                                                          VerificationOutcome mobileVerificationOutcome,
                                                          VerificationOutcome emailVerificationOutcome) {
        //When
        VerificationOutcomeForSurname verificationOutcomeForSurname = VerificationOutcomeForSurname.getVerificationOutcomesForSurname(surname);
        //Then
        assertThat(verificationOutcomeForSurname.getEmailOutcome()).isEqualTo(emailVerificationOutcome);
        assertThat(verificationOutcomeForSurname.getMobileOutcome()).isEqualTo(mobileVerificationOutcome);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Simpson", "Other"})
    void shouldGetDefaultVerificationOutcomesForUnknownSurname(String surname) {
        //When
        VerificationOutcomeForSurname verificationOutcomeForSurname = VerificationOutcomeForSurname.getVerificationOutcomesForSurname(surname);
        //Then
        assertThat(verificationOutcomeForSurname.getEmailOutcome()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(verificationOutcomeForSurname.getMobileOutcome()).isEqualTo(VerificationOutcome.MATCHED);
    }

    @Test
    void shouldGetDefaultVerificationOutcomesForNullSurname() {
        //When
        VerificationOutcomeForSurname verificationOutcomeForSurname = VerificationOutcomeForSurname.getVerificationOutcomesForSurname(null);
        //Then
        assertThat(verificationOutcomeForSurname.getEmailOutcome()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(verificationOutcomeForSurname.getMobileOutcome()).isEqualTo(VerificationOutcome.MATCHED);
    }


    private static Stream<Arguments> verificationOutcomeArguments() {
        return Stream.of(
                Arguments.of(MOBILE_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD, VerificationOutcome.MATCHED),
                Arguments.of(EMAIL_NOT_HELD_SURNAME, VerificationOutcome.MATCHED, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_AND_EMAIL_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED, VerificationOutcome.MATCHED),
                Arguments.of(EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.MATCHED, VerificationOutcome.NOT_MATCHED),
                Arguments.of(MOBILE_AND_EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED, VerificationOutcome.NOT_MATCHED)
        );
    }
}
