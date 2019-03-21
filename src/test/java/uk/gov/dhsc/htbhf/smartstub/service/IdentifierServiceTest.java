package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifierServiceTest {

    IdentifierService service = new IdentifierService();

    @Test
    void shouldGenerateIdentifierFromNino() {
        String nino = "QQ123456A";
        String expected = Base64.getEncoder().encodeToString((nino + "-").getBytes());

        String result = service.getHouseholdIdentifier(nino);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldGenerateIdentifierWhenNoNinoProvided() {
        String expected = Base64.getEncoder().encodeToString("null-".getBytes());

        String result = service.getHouseholdIdentifier(null);

        assertThat(result).isEqualTo(expected);
    }
}