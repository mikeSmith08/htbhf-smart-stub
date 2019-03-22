package uk.gov.dhsc.htbhf.smartstub.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static uk.gov.dhsc.htbhf.smartstub.controller.IntegrationTestAssertions.assertFieldError;
import static uk.gov.dhsc.htbhf.smartstub.controller.IntegrationTestAssertions.assertSuccessfulNumberOfChildrenResponse;
import static uk.gov.dhsc.htbhf.smartstub.controller.IntegrationTestAssertions.assertSuccessfulStatusResponse;
import static uk.gov.dhsc.htbhf.smartstub.helper.DWPEligibilityRequestTestDataFactory.aDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.smartstub.helper.DWPEligibilityRequestTestDataFactory.aDWPEligibilityRequestWithPerson;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.*;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.NOMATCH;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.PENDING;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DWPBenefitControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/dwp/benefits");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnNoChildrenForMatchingNino() {
        DWPEligibilityRequest request = aDWPEligibilityRequest();

        ResponseEntity<BenefitDTO> benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertSuccessfulNumberOfChildrenResponse(benefit, 0, 0);
    }

    @Test
    void shouldReturnTwoChildrenUnderOneForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderOne(2);
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<BenefitDTO> benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertSuccessfulNumberOfChildrenResponse(benefit, 2, 2);
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderFour(2);
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<BenefitDTO> benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertSuccessfulNumberOfChildrenResponse(benefit, 0, 2);
    }

    @Test
    void shouldReturnNoMatchForMatchingNino() {
        PersonDTO person = aPersonNotFound();
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<BenefitDTO> benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertSuccessfulStatusResponse(benefit, NOMATCH);
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isNull();
    }

    @Test
    void shouldReturnSameChildrenUnder1AndChildrenUnder4WhenChildrenUnder1TooHigh() {
        PersonDTO person = aPersonWithChildren(3, 1);
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<BenefitDTO> benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertSuccessfulNumberOfChildrenResponse(benefit, 1, 1);
    }

    @Test
    void shouldReturnIneligibleForMatchingNino() {
        shouldReturnExpectedStatusForMatchingNino(aPersonWhoIsDWPIneligible(), INELIGIBLE);
    }

    @Test
    void shouldReturnEligibleForMatchingNino() {
        shouldReturnExpectedStatusForMatchingNino(aPersonWhoIsDWPEligible(), ELIGIBLE);
    }

    @Test
    void shouldReturnPendingForMatchingNino() {
        shouldReturnExpectedStatusForMatchingNino(aPersonWhoIsDWPPending(), PENDING);
    }

    private void shouldReturnExpectedStatusForMatchingNino(PersonDTO person, EligibilityStatus expectedStatus) {
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<BenefitDTO> benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertSuccessfulStatusResponse(benefit, expectedStatus);
    }

    @Test
    void shouldReturnBadRequestForMissingNino() {
        PersonDTO person = aPersonWithNoNino();
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(errorResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertFieldError(errorResponse.getBody(), "person.nino", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForInvalidNino() {
        PersonDTO person = aPersonWithAnInvalidNino();
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(errorResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertFieldError(errorResponse.getBody(), "person.nino", "must match \"[a-zA-Z]{2}\\d{6}[a-dA-D]\"");
    }

    @Test
    void shouldReturnBadRequestForMissingDateOfBirth() {
        PersonDTO person = aPersonWithNoDateOfBirth();
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(errorResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertFieldError(errorResponse.getBody(), "person.dateOfBirth", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForMissingAddress() {
        PersonDTO person = aPersonWithNoAddress();
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(errorResponse.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertFieldError(errorResponse.getBody(), "person.address", "must not be null");
    }

    @Test
    void shouldReturnServiceUnavailableForExceptionalNino() {
        PersonDTO person = aPersonWhoWillTriggerAnError();
        DWPEligibilityRequest request = aDWPEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> error = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(error.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(error.getBody().getMessage()).isEqualTo("An internal server error occurred");
    }

}
