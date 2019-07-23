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
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertInternalServerErrorResponse;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aCardRequestWithFirstName;
import static uk.gov.dhsc.htbhf.smartstub.helper.CardRequestDTOTestDataFactory.aValidCardRequest;
import static uk.gov.dhsc.htbhf.smartstub.helper.DepositFundsRequestDTOTestDataFactory.aValidDepositFundsRequest;

/**
 * Runs integration tests that make sure the stubbing required for the 3 card services call work properly together for
 * the following scenarios.
 * <ul>
 *   <li>Full success</li>
 *   <ol>
 *     <li>Create Card returns a cardId prefixed with 9</li>
 *     <li>Get balance returns a low balance to allow a full top-up (under £12.40)</li>
 *     <li>Deposit funds works correctly</li>
 *   </ol>
 *
 *   <li>No Top-up Allowed</li>
 *   <ol>
 *     <li>Create card returns a cardId prefixed with 1</li>
 *     <li>Get balance returns an amount that allows no top-up (£1000)</li>
 *   </ol>
 *
 *   <li>Partial Top-up Allowed</li>
 *   <ol>
 *      <li>Create card returns a cardId prefixed with 2</li>
 *      <li>Get balance returns an amount that allows no top-up (£18.60)</li>
 *      <li>Deposit funds works correctly</li>
 *   </ol>
 *
 *   <li>Create card failure</li>
 *   <ol>
 *      <li>Create card fails when given a first name of CardError</li>
 *   </ol>
 *
 *   <li>Check Balance Failure</li>
 *   <ol>
 *     <li>Create card returns a cardId prefixed with 3</li>
 *     <li>Get Balance fails with a 500</li>
 *   </ol>
 *
 *   <li>Deposit funds Failure</li>
 *   <ol>
 *      <li>Create card returns a cardId prefixed with 4</li>
 *      <li>Get balance returns a low balance to allow a full top-up (under £12.40)</li>
 *      <li>Deposit funds fails with a 500</li>
 *   </ol>
 * </ul>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CardServicesScenarioIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/cards");

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Full success.
     * - Create Card returns a cardId prefixed with 9
     * - Get balance returns a low balance to allow a full top-up (under £12.40)
     * - Deposit funds works correctly
     */
    @Test
    void shouldSucceedThroughAllThreeCardServiceCalls() {
        //Create card should succeed and return a cardId prefixed with 9
        CardRequestDTO cardRequest = aValidCardRequest();
        ResponseEntity<CreateCardResponse> createCardResponse = restTemplate.postForEntity(ENDPOINT, cardRequest, CreateCardResponse.class);
        assertCreateCardSuccessfulWithCardPrefix(createCardResponse, 9);
        //Get balance should return a low balance
        String cardAccountId = createCardResponse.getBody().getCardAccountId();
        ResponseEntity<CardBalanceResponse> cardBalanceResponse = restTemplate.getForEntity(buildBalanceEndpoint(cardAccountId), CardBalanceResponse.class);
        assertBalanceResponseSuccessfulForLowBalance(cardBalanceResponse);
        //Deposit funds should be successful
        DepositFundsRequestDTO depositFundsRequest = aValidDepositFundsRequest();
        ResponseEntity<DepositFundsResponse> depositFundsResponse
                = restTemplate.postForEntity(buildDepositEndpoint(cardAccountId), depositFundsRequest, DepositFundsResponse.class);
        assertDepositFundsResponseSuccessful(depositFundsResponse);
    }

    /**
     * No Top-up Allowed.
     * - Create card returns a cardId prefixed with 1
     * - Get balance returns an amount that allows no top-up (£1000)
     */
    @Test
    void shouldReturnLargeBalanceForNoTopupScenario() {
        //Create card should succeed and return a cardId prefixed with 1
        CardRequestDTO cardRequest = aCardRequestWithFirstName("NoTopup");
        ResponseEntity<CreateCardResponse> createCardResponse = restTemplate.postForEntity(ENDPOINT, cardRequest, CreateCardResponse.class);
        assertCreateCardSuccessfulWithCardPrefix(createCardResponse, 1);
        //Get balance should return a high balance
        String cardAccountId = createCardResponse.getBody().getCardAccountId();
        ResponseEntity<CardBalanceResponse> cardBalanceResponse = restTemplate.getForEntity(buildBalanceEndpoint(cardAccountId), CardBalanceResponse.class);
        assertBalanceResponseSuccessfulForSpecificBalance(cardBalanceResponse, 100000);
    }

    /**
     * Partial Top-up Allowed.
     * - Create card returns a cardId prefixed with 2
     * - Get balance returns an amount that allows no top-up (£18.60)
     * - Deposit funds should be successful
     */
    @Test
    void shouldReturnSpecificBalanceForPartialTopupScenario() {
        //Create card should succeed and return a cardId prefixed with 1
        CardRequestDTO cardRequest = aCardRequestWithFirstName("Partial");
        ResponseEntity<CreateCardResponse> createCardResponse = restTemplate.postForEntity(ENDPOINT, cardRequest, CreateCardResponse.class);
        assertCreateCardSuccessfulWithCardPrefix(createCardResponse, 2);
        //Get balance should return a high balance
        String cardAccountId = createCardResponse.getBody().getCardAccountId();
        ResponseEntity<CardBalanceResponse> cardBalanceResponse = restTemplate.getForEntity(buildBalanceEndpoint(cardAccountId), CardBalanceResponse.class);
        assertBalanceResponseSuccessfulForSpecificBalance(cardBalanceResponse, 1860);
        //Deposit funds should be successful
        DepositFundsRequestDTO depositFundsRequest = aValidDepositFundsRequest();
        ResponseEntity<DepositFundsResponse> depositFundsResponse
                = restTemplate.postForEntity(buildDepositEndpoint(cardAccountId), depositFundsRequest, DepositFundsResponse.class);
        assertDepositFundsResponseSuccessful(depositFundsResponse);
    }

    /**
     * Create card failure.
     * - Create card fails when given a first name of CardError
     */
    @Test
    void shouldFailToCreateCard() {
        //Create card should succeed and return a cardId prefixed with 1
        CardRequestDTO cardRequest = aCardRequestWithFirstName("CardError");
        ResponseEntity<ErrorResponse> error = restTemplate.postForEntity(ENDPOINT, cardRequest, ErrorResponse.class);
        assertInternalServerErrorResponse(error);
    }

    /**
     * Check Balance Failure.
     * - Create card returns a cardId prefixed with 3
     * - Get Balance fails with a 500
     */
    @Test
    void shouldCreateCardButFailToGetBalance() {
        //Create card should succeed and return a cardId prefixed with 1
        CardRequestDTO cardRequest = aCardRequestWithFirstName("BalanceError");
        ResponseEntity<CreateCardResponse> createCardResponse = restTemplate.postForEntity(ENDPOINT, cardRequest, CreateCardResponse.class);
        assertCreateCardSuccessfulWithCardPrefix(createCardResponse, 3);
        //Get balance should return a high balance
        String cardAccountId = createCardResponse.getBody().getCardAccountId();
        ResponseEntity<ErrorResponse> error = restTemplate.getForEntity(buildBalanceEndpoint(cardAccountId), ErrorResponse.class);
        assertInternalServerErrorResponse(error);
    }

    /**
     * Deposit funds Failure.
     * - Create card returns a cardId prefixed with 4
     * - Get balance returns a low balance to allow a full top-up (under £12.40)
     * - Deposit funds fails with a 500
     */
    @Test
    void shouldCreateCardAndGetBalanceButFailToDepositFunds() {
        //Create card should succeed and return a cardId prefixed with 1
        CardRequestDTO cardRequest = aCardRequestWithFirstName("PaymentError");
        ResponseEntity<CreateCardResponse> createCardResponse = restTemplate.postForEntity(ENDPOINT, cardRequest, CreateCardResponse.class);
        assertCreateCardSuccessfulWithCardPrefix(createCardResponse, 4);
        //Get balance should return a high balance
        String cardAccountId = createCardResponse.getBody().getCardAccountId();
        ResponseEntity<CardBalanceResponse> cardBalanceResponse = restTemplate.getForEntity(buildBalanceEndpoint(cardAccountId), CardBalanceResponse.class);
        assertBalanceResponseSuccessfulForLowBalance(cardBalanceResponse);
        //Deposit funds should be successful
        DepositFundsRequestDTO depositFundsRequest = aValidDepositFundsRequest();
        ResponseEntity<ErrorResponse> error = restTemplate.postForEntity(buildDepositEndpoint(cardAccountId), depositFundsRequest, ErrorResponse.class);
        assertInternalServerErrorResponse(error);
    }

    private void assertDepositFundsResponseSuccessful(ResponseEntity<DepositFundsResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(OK);
        DepositFundsResponse depositFundsResponse = response.getBody();
        assertThat(depositFundsResponse).isNotNull();
        assertThat(depositFundsResponse.getReferenceId()).isNotNull();
    }

    private void assertCreateCardSuccessfulWithCardPrefix(ResponseEntity<CreateCardResponse> response, int cardPrefix) {
        assertThat(response.getStatusCode()).isEqualTo(OK);
        CreateCardResponse cardResponse = response.getBody();
        assertThat(cardResponse).isNotNull();
        assertThat(cardResponse.getCardAccountId()).startsWith(cardPrefix + "-");
    }

    private void assertBalanceResponseSuccessfulForLowBalance(ResponseEntity<CardBalanceResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(OK);
        CardBalanceResponse balanceResponse = response.getBody();
        assertThat(balanceResponse).isNotNull();
        assertThat(balanceResponse.getAvailableBalanceInPence()).isBetween(0, 1239);
        assertThat(balanceResponse.getLedgerBalanceInPence()).isBetween(0, 1239);
        assertThat(balanceResponse.getLedgerBalanceInPence()).isEqualTo(balanceResponse.getAvailableBalanceInPence());
    }

    private void assertBalanceResponseSuccessfulForSpecificBalance(ResponseEntity<CardBalanceResponse> response, int expectedBalance) {
        assertThat(response.getStatusCode()).isEqualTo(OK);
        CardBalanceResponse balanceResponse = response.getBody();
        assertThat(balanceResponse).isNotNull();
        assertThat(balanceResponse.getAvailableBalanceInPence()).isEqualTo(expectedBalance);
        assertThat(balanceResponse.getLedgerBalanceInPence()).isEqualTo(expectedBalance);
        assertThat(balanceResponse.getLedgerBalanceInPence()).isEqualTo(balanceResponse.getAvailableBalanceInPence());
    }

    private String buildBalanceEndpoint(String cardId) {
        return ENDPOINT + "/" + cardId + "/balance";
    }

    private String buildDepositEndpoint(String cardId) {
        return ENDPOINT + "/" + cardId + "/deposit";
    }

}
