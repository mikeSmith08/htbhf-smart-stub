package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.NO_MATCH;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.PENDING;
import static uk.gov.dhsc.htbhf.smartstub.controller.IntegrationTestAssertions.assertCorrectNumberOfChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.*;

class BenefitsServiceTest {

    private BenefitsService benefitsService = new BenefitsService(new IdentifierService());

    @ParameterizedTest
    @ValueSource(strings = {
            "EA123456C",
            "IE123456C",
            "PE123456C",
            "EE123456C",
            "XE123456C"
    })
    void shouldReturnEligibleForMatchingNino(String nino) {
        PersonDTO person = aPersonWithNino(nino);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(ELIGIBLE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "IA123456C",
            "XI123456C",
            "PI123456C",
            "II123456C",
            "XI123456C"
    })
    void shouldReturnInEligibleForMatchingNino(String nino) {
        PersonDTO person = aPersonWithNino(nino);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(INELIGIBLE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PA123456C",
            "XP123456C",
            "VP123456C",
            "PP123456C",
            "XP123456C"
    })
    void shouldReturnPendingForMatchingNino(String nino) {
        PersonDTO person = aPersonWithNino(nino);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(PENDING);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "BA123456C",
            "XW123456C",
            "VN123456C",
            "OK123456C",
            "XL123456C"
    })
    void shouldReturnNoMatchForMatchingNino(String nino) {
        PersonDTO person = aPersonWithNino(nino);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(NO_MATCH);
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderFour(2);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertCorrectNumberOfChildren(benefit, 0, 2);
    }

    @Test
    void shouldReturnThreeChildrenUnderOneForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderOne(3);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(3);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(3);
        assertCorrectNumberOfChildren(benefit, 3, 3);
    }

    @Test
    void shouldReturnSameNumberOfChildrenUnder1AndUnder4WhenRequestHasUnder1LargerThanUnder4() {
        PersonDTO person = aPersonWithChildren(4, 1);

        BenefitDTO benefit = benefitsService.getDWPBenefits(person.getNino());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(1);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(1);
        assertCorrectNumberOfChildren(benefit, 1, 1);
    }

    @Test
    void shouldThrowExceptionWhenErrorNinoSupplied() {
        PersonDTO person = aPersonWhoWillTriggerAnError();

        assertThrows(IllegalArgumentException.class, () -> benefitsService.getDWPBenefits(person.getNino()));
    }
}
