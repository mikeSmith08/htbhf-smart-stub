package uk.gov.dhsc.htbhf.smartstub.controller;

import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.ResponseEntity;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class IntegrationTestAssertions {

    public static void assertSuccessfulStatusResponse(ResponseEntity<BenefitDTO> benefit, EligibilityStatus nomatch) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getEligibilityStatus()).isEqualTo(nomatch);
    }

    public static void assertSuccessfulNumberOfChildrenResponse(ResponseEntity<BenefitDTO> benefit, Integer childrenUnderOne, Integer childrenUnderFour) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isEqualTo(childrenUnderOne);
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isEqualTo(childrenUnderFour);
    }

    public static void assertFieldError(ErrorResponse body, String expectedField, String expectedFieldMessage) {
        assertThat(body.getFieldErrors()).hasSize(1);
        ErrorResponse.FieldError fieldError = body.getFieldErrors().get(0);
        AssertionsForClassTypes.assertThat(fieldError.getField()).isEqualTo(expectedField);
        AssertionsForClassTypes.assertThat(fieldError.getMessage()).isEqualTo(expectedFieldMessage);
    }
}
