package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonNotFound;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsEligible;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsIneligible;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsPending;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoWillTriggerAnError;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderFour;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderOne;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.NOMATCH;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.PENDING;

class BenefitsServiceTest {

    private BenefitsService benefitsService = new BenefitsService(new IdentifierService());

    @Test
    void shouldReturnIneligibleForMatchingNino() {
        PersonDTO person = aPersonWhoIsIneligible();

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(INELIGIBLE);
    }

    @Test
    void shouldReturnEligibleForMatchingNino() {
        PersonDTO person = aPersonWhoIsEligible();

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(ELIGIBLE);
    }

    @Test
    void shouldReturnPendingForMatchingNino() {
        PersonDTO person = aPersonWhoIsPending();

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(PENDING);
    }

    @Test
    void shouldReturnNoMatchNino() {
        PersonDTO person = aPersonNotFound();

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getEligibilityStatus()).isEqualTo(NOMATCH);
        assertThat(benefit.getNumberOfChildrenUnderOne()).isNull();
        assertThat(benefit.getNumberOfChildrenUnderFour()).isNull();
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderFour(2);

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(2);
    }

    @Test
    void shouldReturnThreeChildrenUnderOneForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderOne(3);

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(3);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(3);
    }

    @Test
    void shouldReturnSameNumberOfChildrenUnder1AndUnder4WhenRequestHasUnder1LargerThanUnder4() {
        PersonDTO person = aPersonWithChildren(4, 1);

        BenefitDTO benefit = benefitsService.getBenefits(person.getNino());

        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(1);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionWhenErrorNinoSupplied() {
        PersonDTO person = aPersonWhoWillTriggerAnError();

        assertThrows(IllegalArgumentException.class, () -> benefitsService.getBenefits(person.getNino()));
    }
}
