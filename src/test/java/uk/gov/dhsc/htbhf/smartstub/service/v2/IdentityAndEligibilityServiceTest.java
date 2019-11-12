package uk.gov.dhsc.htbhf.smartstub.service.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.v2.PersonDTOV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.VerificationOutcome;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_SURNAME;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SINGLE_SIX_MONTH_OLD;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.TWO_CHILDREN;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2WithPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.IdentityAndEligibilityResponseTestDataFactory.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.aPersonDTOV2WithNino;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.aPersonDTOV2WithSurnameAndEmail;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.aPersonDTOV2WithSurnameAndMobile;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.aPersonDTOV2WithSurnameAndNino;
import static uk.gov.dhsc.htbhf.smartstub.service.v2.IdentityAndEligibilityService.*;

class IdentityAndEligibilityServiceTest {

    private static final String IDENTITY_MATCH_FAILED_NINO = "QQ123456A";
    private static final String IDENTITY_MATCHED_NOT_ELIGIBLE_NINO = "MN123456A";
    private static final String IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_NINO = "MC999999A";
    private static final String IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_PARTIAL_CHILDREN_MATCH_NINO = "MC219999A";
    private static final String IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_FULL_CHILDREN_MATCH_NINO = "MC129999A";
    private static final String IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_NO_CHILDREN_NINO = "MC009999A";
    private static final String NOT_SET = null;

    private IdentityAndEligibilityService service = new IdentityAndEligibilityService();

    @Test
    void shouldFailIdentityMatch() {
        PersonDTOV2 person = aPersonDTOV2WithNino(IDENTITY_MATCH_FAILED_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchFailedResponse();
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @Test
    void shouldReturnIdentityMatchedEligibilityNotConfirmed() {
        PersonDTOV2 person = aPersonDTOV2WithNino(IDENTITY_MATCHED_NOT_ELIGIBLE_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityNotConfirmedResponse();
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @Test
    void shouldReturnIdentityMatchedEligibilityConfirmedAddressNotMatched() {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndNino(ADDRESS_LINE_ONE_NOT_MATCHED_SURNAME, IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedAddressNotMatchedResponse();
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @Test
    void shouldReturnIdentityMatchedEligibilityConfirmedPostcodeNotMatched() {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndNino(POSTCODE_NOT_MATCHED_SURNAME, IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedPostcodeNotMatchedResponse();
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @Test
    void shouldReturnIdentityMatchedEligibilityConfirmedForPregnantWomanWithNoChildren() {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndNino(SIMPSON_SURNAME, IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_NO_CHILDREN_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches(Collections.emptyList());
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @Test
    void shouldThrowExceptionForExceptionalNino() {
        //Given
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndNino(SIMPSON_SURNAME, EXCEPTION_NINO);
        DWPEligibilityRequestV2 requestV2 = aValidDWPEligibilityRequestV2WithPerson(person);
        //When
        IllegalArgumentException thrown = catchThrowableOfType(() -> service.evaluateEligibility(requestV2), IllegalArgumentException.class);
        //Then
        assertThat(thrown).hasMessage("NINO provided (XX999999D) has been configured to trigger an Exception");
    }

    @ParameterizedTest(name = "Surname={0}, mobile outcome={1}, email outcome={2}")
    @MethodSource("verificationOutcomeForSurnameArguments")
    void shouldReturnIdentityMatchedEligibilityConfirmedPartialChildrenMatch(String surname,
                                                                             VerificationOutcome mobileMatchOutcome,
                                                                             VerificationOutcome emailMatchOutcome) {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndNino(surname, IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_PARTIAL_CHILDREN_MATCH_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedUCResponseWithMatches(mobileMatchOutcome,
                emailMatchOutcome, SINGLE_SIX_MONTH_OLD);
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @ParameterizedTest(name = "Surname={0}, mobile outcome={1}, email outcome={2}")
    @MethodSource("verificationOutcomeForSurnameArguments")
    void shouldReturnIdentityMatchedEligibilityConfirmedFullChildrenMatch(String surname,
                                                                          VerificationOutcome mobileMatchOutcome,
                                                                          VerificationOutcome emailMatchOutcome) {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndNino(surname, IDENTITY_MATCHED_ELIGIBILITY_CONFIRMED_FULL_CHILDREN_MATCH_NINO);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedUCResponseWithMatches(mobileMatchOutcome,
                emailMatchOutcome, TWO_CHILDREN);
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @ParameterizedTest(name = "Surname={0}, email outcome={1}")
    @MethodSource("emailVerificationOutcomeForSurnameArguments")
    void shouldReturnNotProvidedMobileVerificationWhenNoMobileProvided(String surname,
                                                                       VerificationOutcome emailMatchOutcome) {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndMobile(surname, NOT_SET);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedUCResponseWithMatches(VerificationOutcome.NOT_SUPPLIED,
                emailMatchOutcome, TWO_CHILDREN);
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    @ParameterizedTest(name = "Surname={0}, mobile outcome={1}")
    @MethodSource("mobileVerificationOutcomeForSurnameArguments")
    void shouldReturnNotProvidedEmailVerificationWhenNoEmailAddressProvided(String surname,
                                                                            VerificationOutcome mobileMatchOutcome) {
        PersonDTOV2 person = aPersonDTOV2WithSurnameAndEmail(surname, NOT_SET);
        IdentityAndEligibilityResponse expectedResponse = anIdentityMatchedEligibilityConfirmedUCResponseWithMatches(mobileMatchOutcome,
                VerificationOutcome.NOT_SUPPLIED, TWO_CHILDREN);
        runEvaluateEligibilityTest(person, expectedResponse);
    }

    private void runEvaluateEligibilityTest(PersonDTOV2 person, IdentityAndEligibilityResponse expectedResponse) {
        //Given
        DWPEligibilityRequestV2 requestV2 = aValidDWPEligibilityRequestV2WithPerson(person);
        //When
        IdentityAndEligibilityResponse response = service.evaluateEligibility(requestV2);
        //Then
        assertThat(response).isEqualTo(expectedResponse);
    }

    //Arguments are Surname, Mobile Match, Email Match
    private static Stream<Arguments> verificationOutcomeForSurnameArguments() {
        return Stream.of(
                Arguments.of(MOBILE_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD, VerificationOutcome.MATCHED),
                Arguments.of(EMAIL_NOT_HELD_SURNAME, VerificationOutcome.MATCHED, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_AND_EMAIL_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED, VerificationOutcome.MATCHED),
                Arguments.of(EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.MATCHED, VerificationOutcome.NOT_MATCHED),
                Arguments.of(MOBILE_AND_EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED, VerificationOutcome.NOT_MATCHED)
        );
    }

    //Arguments are Surname, Email Match
    private static Stream<Arguments> emailVerificationOutcomeForSurnameArguments() {
        return Stream.of(
                Arguments.of(MOBILE_NOT_HELD_SURNAME, VerificationOutcome.MATCHED),
                Arguments.of(EMAIL_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_AND_EMAIL_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_NOT_MATCHED_SURNAME, VerificationOutcome.MATCHED),
                Arguments.of(EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED),
                Arguments.of(MOBILE_AND_EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED)
        );
    }

    //Arguments are Surname, Mobile Match
    private static Stream<Arguments> mobileVerificationOutcomeForSurnameArguments() {
        return Stream.of(
                Arguments.of(MOBILE_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD),
                Arguments.of(EMAIL_NOT_HELD_SURNAME, VerificationOutcome.MATCHED),
                Arguments.of(MOBILE_AND_EMAIL_NOT_HELD_SURNAME, VerificationOutcome.NOT_HELD),
                Arguments.of(MOBILE_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED),
                Arguments.of(EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.MATCHED),
                Arguments.of(MOBILE_AND_EMAIL_NOT_MATCHED_SURNAME, VerificationOutcome.NOT_MATCHED)
        );
    }

}
