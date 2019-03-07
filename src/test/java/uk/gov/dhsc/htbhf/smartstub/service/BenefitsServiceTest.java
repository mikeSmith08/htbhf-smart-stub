package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitType;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonOnNoBenefitsAndNoChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonOnUniversalCreditWithNoChildren;

class BenefitsServiceTest {

    private BenefitsService benefitsService = new BenefitsService();

    @Test
    void shouldReturnNoBenefitsAndNoChildrenForMatchingNino() {
        var person = aPersonOnNoBenefitsAndNoChildren();

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getBenefit()).isNull();
        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(0);
    }

    @Test
    void shouldReturnUniversalCreditForMatchingNino() {
        var person = aPersonOnUniversalCreditWithNoChildren();

        var benefit = benefitsService.getBenefits(person.getNino().toCharArray());

        assertThat(benefit.getBenefit()).isEqualTo(BenefitType.UNIVERSAL_CREDIT);
        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(0);
    }
}
