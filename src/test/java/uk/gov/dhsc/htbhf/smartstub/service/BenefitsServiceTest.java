package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonNotFound;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsEligible;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsIneligible;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsPending;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderFour;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderOne;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.NOMATCH;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.PENDING;

class BenefitsServiceTest {

    private BenefitsService benefitsService = new BenefitsService();

    @Test
    void shouldReturnIneligibleForMatchingNino() {
        var person = aPersonWhoIsIneligible();

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(INELIGIBLE);
    }

    @Test
    void shouldReturnEligibleForMatchingNino() {
        var person = aPersonWhoIsEligible();

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(ELIGIBLE);
    }

    @Test
    void shouldReturnPendingForMatchingNino() {
        var person = aPersonWhoIsPending();

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(PENDING);
    }

    @Test
    void shouldReturnNoMatchNino() {
        var person = aPersonNotFound();

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(NOMATCH);
        assertThat(benefit.getNumberOfChildrenUnderOne()).isNull();
        assertThat(benefit.getNumberOfChildrenUnderFour()).isNull();
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        var person = aPersonWithChildrenUnderFour(2);

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(2);
    }

    @Test
    void shouldReturnThreeChildrenUnderOneForMatchingNino() {
        var person = aPersonWithChildrenUnderOne(3);

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(3);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(3);
    }

    @Test
    void shouldThrowExceptionWhenChildrenUnderOneIsGreaterThanChildrenUnderFour() {
        var person = aPersonWithChildren(4, 1);

        assertThrows(IllegalArgumentException.class, () -> benefitsService.getBenefits(person.getNino().toCharArray()));

    }
}
