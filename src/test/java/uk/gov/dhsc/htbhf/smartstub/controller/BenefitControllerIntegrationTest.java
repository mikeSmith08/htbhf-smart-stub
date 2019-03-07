package uk.gov.dhsc.htbhf.smartstub.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitType;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonNotFound;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonOnNoBenefitsAndNoChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonOnUniversalCreditWithNoChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithAnInvalidNino;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithChildrenUnderFour;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWithNoNino;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BenefitControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/dwp/benefits/v1");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnNoBenefitsAndNoChildrenForMatchingNino() {
        var person = aPersonOnNoBenefitsAndNoChildren();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getBenefit()).isNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isEqualTo(0);
    }

    @Test
    void shouldReturnUniversalCreditForMatchingNino() {
        var person = aPersonOnUniversalCreditWithNoChildren();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getBenefit()).isEqualTo(BenefitType.UNIVERSAL_CREDIT);
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isEqualTo(0);
    }

    @Test
    void shouldReturnTwoChildrenUnderFourForMatchingNino() {
        var person = aPersonWithChildrenUnderFour(2);

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isNotNull();
        assertThat(benefit.getBody().getNumberOfChildrenUnderOne()).isEqualTo(0);
        assertThat(benefit.getBody().getNumberOfChildrenUnderFour()).isEqualTo(2);
    }

    @Test
    void shouldReturnNotFoundResponseForMatchingNino() {
        var person = aPersonNotFound();

        var benefit = restTemplate.postForEntity(ENDPOINT, person, BenefitDTO.class);

        assertThat(benefit.getStatusCode()).isEqualTo(NOT_FOUND);
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
}
