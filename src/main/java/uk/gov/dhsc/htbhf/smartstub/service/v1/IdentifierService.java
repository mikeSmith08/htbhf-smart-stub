package uk.gov.dhsc.htbhf.smartstub.service.v1;

import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Generates household identifiers for DWP based on the NINO provided.
 */
@Service
public class IdentifierService {

    public static final String DWP_DELIMITER = "-";

    private final Base64.Encoder encoder = Base64.getEncoder();

    public String getDWPHouseholdIdentifier(String nino) {
        return encodeIdentifier(nino + DWP_DELIMITER);
    }

    private String encodeIdentifier(String valueToEncode) {
        return encoder.encodeToString(valueToEncode.getBytes());
    }
}
