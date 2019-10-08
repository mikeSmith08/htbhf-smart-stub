package uk.gov.dhsc.htbhf.smartstub.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.smartstub.factory.PostcodeDataFactory.postcodeData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostcodesControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/postcodes/");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSuccessfullyGetLocationData() {
        String postcode = "bs14tb";

        ResponseEntity<String> addressDataResponse = restTemplate.getForEntity(ENDPOINT + postcode, String.class);

        assertThat(addressDataResponse.getStatusCode()).isEqualTo(OK);
        assertThat(addressDataResponse.getBody()).isEqualTo(postcodeData(postcode));
    }
}
