package uk.gov.dhsc.htbhf.smartstub.controller.v1;

import org.springframework.http.ResponseEntity;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.smartstub.model.v1.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.v1.ChildDTO;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class IntegrationTestAssertions {

    public static void assertSuccessfulResponse(ResponseEntity<BenefitDTO> benefit, EligibilityStatus eligibilityStatus) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        BenefitDTO benefitDTO = benefit.getBody();
        assertThat(benefitDTO).isNotNull();
        assertThat(benefitDTO.getEligibilityStatus()).isEqualTo(eligibilityStatus);
    }

    public static void assertSuccessfulResponseWithCorrectNumberOfChildren(ResponseEntity<BenefitDTO> benefit, EligibilityStatus eligibilityStatus,
                                                                           Integer childrenUnderOne, Integer childrenUnderFour) {
        assertSuccessfulResponse(benefit, eligibilityStatus);
        assertCorrectNumberOfChildren(benefit.getBody(), childrenUnderOne, childrenUnderFour);
    }

    public static void assertCorrectNumberOfChildren(BenefitDTO benefit, Integer childrenUnderOne, Integer childrenUnderFour) {
        assertThat(benefit.getNumberOfChildrenUnderOne()).isEqualTo(childrenUnderOne);
        assertThat(benefit.getNumberOfChildrenUnderFour()).isEqualTo(childrenUnderFour);
        assertCorrectNumberOfChildren(benefit.getChildren(), childrenUnderOne, childrenUnderFour);
    }

    private static void assertCorrectNumberOfChildren(List<ChildDTO> children, Integer childrenUnderOne, Integer childrenUnderFour) {
        assertThat(numberOfChildrenUnderOne(children)).isEqualTo(childrenUnderOne);
        assertThat(numberOfChildrenUnderFour(children)).isEqualTo(childrenUnderFour);
    }

    private static Integer numberOfChildrenUnderOne(List<ChildDTO> children) {
        return numberOfChildrenUnderAgeInYears(children, 1);
    }

    private static Integer numberOfChildrenUnderFour(List<ChildDTO> children) {
        return numberOfChildrenUnderAgeInYears(children, 4);
    }

    private static Integer numberOfChildrenUnderAgeInYears(List<ChildDTO> children, Integer ageInYears) {
        LocalDate pastDate = LocalDate.now().minusYears(ageInYears);
        return Math.toIntExact(children.stream()
                .filter(child -> child.getDateOfBirth().isAfter(pastDate))
                .count());
    }
}
