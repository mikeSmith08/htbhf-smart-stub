package uk.gov.dhsc.htbhf.smartstub.controller;

import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.ResponseEntity;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.ChildDTO;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class IntegrationTestAssertions {

    public static void assertSuccessfulStatusResponse(ResponseEntity<BenefitDTO> benefit, EligibilityStatus nomatch) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getEligibilityStatus()).isEqualTo(nomatch);
    }

    public static void assertFieldError(ErrorResponse body, String expectedField, String expectedFieldMessage) {
        assertThat(body.getFieldErrors()).hasSize(1);
        ErrorResponse.FieldError fieldError = body.getFieldErrors().get(0);
        AssertionsForClassTypes.assertThat(fieldError.getField()).isEqualTo(expectedField);
        AssertionsForClassTypes.assertThat(fieldError.getMessage()).isEqualTo(expectedFieldMessage);
    }

    public static void assertSuccessfulNumberOfChildrenResponse(ResponseEntity<BenefitDTO> benefit, Integer childrenUnderOne, Integer childrenUnderFour) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
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
