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
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonOnNoBenefitsAndNoChildren;
import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonOnUniversalCreditWithNoChildren;

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
}
