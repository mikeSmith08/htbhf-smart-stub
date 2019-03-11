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

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
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
        var person = aPersonWhoIsIneligible();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertNumberOfChildrenResponse(benefit, 0, 0);
    }

    @Test
    void shouldReturnTwoChildrenUnderOneForMatchingNino() {
        var person = aPersonWithChildrenUnderOne(2);

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertNumberOfChildrenResponse(benefit, 2, 2);
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        var person = aPersonWithChildrenUnderFour(2);

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertNumberOfChildrenResponse(benefit, 0, 2);
    }

    @Test
    void shouldReturnNoMatchForMatchingNino() {
        var person = aPersonNotFound();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertStatusResponse(benefit, NOMATCH);
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isNull();
    }

    @Test
    void shouldReturnIneligibleForMatchingNino() {
        var person = aPersonWhoIsIneligible();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertStatusResponse(benefit, INELIGIBLE);
    }

    @Test
    void shouldReturnEligibleForMatchingNino() {
        var person = aPersonWhoIsEligible();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertStatusResponse(benefit, ELIGIBLE);
    }

    @Test
    void shouldReturnPendingForMatchingNino() {
        var person = aPersonWhoIsPending();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertStatusResponse(benefit, PENDING);
    }

    @Test
    void shouldReturnBadRequestForMissingNino() {
        var person = aPersonWithNoNino();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForInvalidNino() {
        var person = aPersonWithAnInvalidNino();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestWhenChildrenUnderOneIsGreaterThanChildrenFour() {
        var person = aPersonWithChildren(3, 1);

        var benefit = restTemplate.postForEntity(ENDPOINT, person, ErrorResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getMessage()).isEqualTo("Can not have more children under one than children four. Given values were 3, 1");
    }

    @Test
    void shouldReturnBadRequestForMissingDateOfBirth() {
        var person = aPersonWithNoDateOfBirth();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingAddress() {
        var person = aPersonWithNoAddress();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

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
