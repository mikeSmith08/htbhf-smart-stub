package uk.gov.dhsc.htbhf.smartstub.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;
import uk.gov.dhsc.htbhf.smartstub.model.*;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertValidationErrorInResponse;
import static uk.gov.dhsc.htbhf.smartstub.helper.AddressDTOTestDataFactory.anAddressWithAddressLine1;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithAddress;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithLastName;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aValidCardRequest;
import static uk.gov.dhsc.htbhf.smartstub.helper.DepositFundsRequestDTOTestDataFactory.aDepositFundsRequestWithAmount;
import static uk.gov.dhsc.htbhf.smartstub.helper.DepositFundsRequestDTOTestDataFactory.aValidDepositFundsRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CardServicesControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/cards");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSuccessfullyCreateCard() {
        //Given
        CardRequestDTO request = aValidCardRequest();
        //When
        ResponseEntity<CreateCardResponse> response = restTemplate.postForEntity(ENDPOINT, request, CreateCardResponse.class);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        CreateCardResponse cardResponse = response.getBody();
        assertThat(cardResponse).isNotNull();
        assertThat(cardResponse.getCardAccountId()).isNotNull();
    }

    @Test
    void shouldFailWithValidationErrorWithMissingFirstName() {
        //Given
        CardRequestDTO request = aCardRequestWithLastName(null);
        //When
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);
        //Then
        assertValidationErrorInResponse(errorResponse, "lastName", "must not be null");
    }

    @Test
    void shouldFailWithValidationErrorWithInvalidAddress() {
        //Given
        AddressDTO addressDTO = anAddressWithAddressLine1(null);
        CardRequestDTO request = aCardRequestWithAddress(addressDTO);
        //When
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);
        //Then
        assertValidationErrorInResponse(errorResponse, "address.addressLine1", "must not be null");
    }

    @Test
    void shouldSuccessfullyGetBalance() {
        //Given
        String cardId = "myId";
        //When
        ResponseEntity<CardBalanceResponse> response = restTemplate.getForEntity(buildBalanceEndpoint(cardId), CardBalanceResponse.class);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        CardBalanceResponse balanceResponse = response.getBody();
        assertThat(balanceResponse).isNotNull();
        assertThat(balanceResponse.getAvailableBalanceInPence()).isBetween(0, 1239);
        assertThat(balanceResponse.getLedgerBalanceInPence()).isBetween(0, 1239);
        assertThat(balanceResponse.getLedgerBalanceInPence()).isEqualTo(balanceResponse.getAvailableBalanceInPence());
    }

    @Test
    void shouldFailToGetBalanceWithNoCardId() {
        //Given
        String cardId = "";
        //When
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(buildBalanceEndpoint(cardId), ErrorResponse.class);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void shouldSuccessfullyDepositFunds() {
        //Given
        String cardId = "myId";
        DepositFundsRequestDTO request = aValidDepositFundsRequest();
        //When
        ResponseEntity<DepositFundsResponse> response = restTemplate.postForEntity(buildDepositEndpoint(cardId), request, DepositFundsResponse.class);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        DepositFundsResponse depositFundsResponse = response.getBody();
        assertThat(depositFundsResponse).isNotNull();
        assertThat(depositFundsResponse.getReferenceId()).isNotNull();
    }

    @Test
    void shouldFailDepositWithValidationErrorWithInvalidRequest() {
        //Given
        String cardId = "myId";
        DepositFundsRequestDTO request = aDepositFundsRequestWithAmount(null);
        //When
        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(buildDepositEndpoint(cardId), request, ErrorResponse.class);
        //Then
        assertValidationErrorInResponse(errorResponse, "amountInPence", "must not be null");
    }

    @Test
    void shouldFailDepositWithNoCardId() {
        //Given
        String cardId = "";
        DepositFundsRequestDTO request = aValidDepositFundsRequest();
        //When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(buildDepositEndpoint(cardId), request, ErrorResponse.class);
        //Then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private String buildBalanceEndpoint(String cardId) {
        return ENDPOINT + "/" + cardId + "/balance";
    }

    private String buildDepositEndpoint(String cardId) {
        return ENDPOINT + "/" + cardId + "/deposit";
    }

}
