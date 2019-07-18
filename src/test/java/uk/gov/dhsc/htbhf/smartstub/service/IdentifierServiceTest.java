package uk.gov.dhsc.htbhf.smartstub.service;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.smartstub.service.IdentifierService.DWP_DELIMITER;

class IdentifierServiceTest {

    private static final String NINO = "QQ123456A";

    private IdentifierService service = new IdentifierService();

    @Test
    void shouldGenerateDWPIdentifierFromNino() {
        String expected = Base64.getEncoder().encodeToString((NINO + DWP_DELIMITER).getBytes());

        String result = service.getDWPHouseholdIdentifier(NINO);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldGenerateDWPIdentifierWhenNoNinoProvided() {
        String expected = Base64.getEncoder().encodeToString(("null" + DWP_DELIMITER).getBytes());

        String result = service.getDWPHouseholdIdentifier(null);

        assertThat(result).isEqualTo(expected);
    }
}
