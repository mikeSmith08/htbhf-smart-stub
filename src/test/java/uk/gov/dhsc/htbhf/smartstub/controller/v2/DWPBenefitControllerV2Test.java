package uk.gov.dhsc.htbhf.smartstub.controller.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;
import uk.gov.dhsc.htbhf.smartstub.service.v2.IdentityAndEligibilityService;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.TestConstants.SIMPSON_SURNAME;
import static uk.gov.dhsc.htbhf.TestConstants.TWO_CHILDREN_BORN_AT_START_OF_MONTH;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertInternalServerErrorResponse;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertValidationErrorInResponse;
import static uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome.NOT_SET;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.aValidEligibilityHttpEntity;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.anEligibilityHttpEntityWithNinoAndSurname;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.anInvalidEligibilityHttpEntity;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchFailedResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchedEligibilityConfirmedUCResponseWithAllMatches;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchedEligibilityConfirmedUCResponseWithMatches;
import static uk.gov.dhsc.htbhf.smartstub.Assertions.assertIsEqualIgnoringHouseholdIdentifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DWPBenefitControllerV2Test {

    private static final URI ENDPOINT = URI.create("/v2/dwp/benefits");
    private static final String IDENTITY_NOT_MATCHED_NINO = "XA123456D";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnOkResponseWithAllMatchResponse() {
        //Given
        HttpEntity request = aValidEligibilityHttpEntity();

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        IdentityAndEligibilityResponse expected = anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(NOT_SET, TWO_CHILDREN_BORN_AT_START_OF_MONTH);
        assertIsEqualIgnoringHouseholdIdentifier(responseEntity.getBody(), expected);
    }

    @Test
    void shouldReturnOkResponseWithIdentityStatusNotMatchedResponse() {
        //Given - making sure we test that the NINO is used from the request
        HttpEntity request = anEligibilityHttpEntityWithNinoAndSurname(IDENTITY_NOT_MATCHED_NINO, SIMPSON_SURNAME);

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnOkResponseWithMobileNotMatchedVerificationResponse() {
        //Given - making sure we test that the surname is used from the request
        HttpEntity request = anEligibilityHttpEntityWithNinoAndSurname(HOMER_NINO, "MobileNotMatched");

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        IdentityAndEligibilityResponse expected = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_MATCHED,
                VerificationOutcome.MATCHED,
                TWO_CHILDREN_BORN_AT_START_OF_MONTH
        );
        assertIsEqualIgnoringHouseholdIdentifier(responseEntity.getBody(), expected);
    }

    @Test
    void shouldReturnBadRequestResponseWithInvalidRequest() {
        //Given
        HttpEntity request = anInvalidEligibilityHttpEntity();

        //When
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(ENDPOINT, HttpMethod.GET, request, ErrorResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertValidationErrorInResponse(responseEntity, "person.nino",
                "must match \"^(?!BG|GB|NK|KN|TN|NT|ZZ)[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z](\\d{6})[A-D]$\"");
    }

    @Test
    void shouldReturnBadRequestResponseWithExceptionalNino() {
        //Given
        HttpEntity request = anEligibilityHttpEntityWithNinoAndSurname(IdentityAndEligibilityService.EXCEPTION_NINO, SIMPSON_SURNAME);

        //When
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(ENDPOINT, HttpMethod.GET, request, ErrorResponse.class);

        //Then
        assertInternalServerErrorResponse(responseEntity);
    }

}
