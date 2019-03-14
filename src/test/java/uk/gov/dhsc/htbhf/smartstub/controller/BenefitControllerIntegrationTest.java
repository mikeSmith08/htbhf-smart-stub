package uk.gov.dhsc.htbhf.smartstub.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.smartstub.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.smartstub.helper.EligibilityRequestTestDataFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.smartstub.helper.EligibilityRequestTestDataFactory.anEligibilityRequestWithPerson;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonNotFound;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsEligible;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsIneligible;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsPending;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithAnInvalidNino;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderFour;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderOne;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithNoAddress;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithNoDateOfBirth;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithNoNino;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.NOMATCH;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.PENDING;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BenefitControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/dwp/benefits");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnNoChildrenForMatchingNino() {
        EligibilityRequest anEligibilityRequest = anEligibilityRequest();

        var benefit = restTemplate.postForEntity(ENDPOINT, anEligibilityRequest, BenefitDTO.class);

        assertNumberOfChildrenResponse(benefit, 0, 0);
    }

    @Test
    void shouldReturnTwoChildrenUnderOneForMatchingNino() {
        var person = aPersonWithChildrenUnderOne(2);
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertNumberOfChildrenResponse(benefit, 2, 2);
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        PersonDTO person = aPersonWithChildrenUnderFour(2);
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertNumberOfChildrenResponse(benefit, 0, 2);
    }

    @Test
    void shouldReturnNoMatchForMatchingNino() {
        PersonDTO person = aPersonNotFound();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertStatusResponse(benefit, NOMATCH);
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isNull();
    }

    @Test
    void shouldReturnIneligibleForMatchingNino() {
        PersonDTO person = aPersonWhoIsIneligible();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertStatusResponse(benefit, INELIGIBLE);
    }

    @Test
    void shouldReturnEligibleForMatchingNino() {
        PersonDTO person = aPersonWhoIsEligible();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertStatusResponse(benefit, ELIGIBLE);
    }

    @Test
    void shouldReturnPendingForMatchingNino() {
        PersonDTO person = aPersonWhoIsPending();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertStatusResponse(benefit, PENDING);
    }

    @Test
    void shouldReturnBadRequestForMissingNino() {
        PersonDTO person = aPersonWithNoNino();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForInvalidNino() {
        PersonDTO person = aPersonWithAnInvalidNino();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestWhenChildrenUnderOneIsGreaterThanChildrenFour() {
        PersonDTO person = aPersonWithChildren(3, 1);
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getMessage()).isEqualTo("Can not have more children under one than children four. Given values were 3, 1");
    }

    @Test
    void shouldReturnBadRequestForMissingDateOfBirth() {
        PersonDTO person = aPersonWithNoDateOfBirth();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingAddress() {
        PersonDTO person = aPersonWithNoAddress();
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        var benefit = restTemplate.postForEntity(ENDPOINT, request, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    private void assertStatusResponse(ResponseEntity<BenefitDTO> benefit, EligibilityStatus nomatch) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getEligibilityStatus()).isEqualTo(nomatch);
    }

    private void assertNumberOfChildrenResponse(ResponseEntity<BenefitDTO> benefit, Integer childrenUnderOne, Integer childrenUnderFour) {
        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isEqualTo(childrenUnderOne);
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isEqualTo(childrenUnderFour);
    }
}
