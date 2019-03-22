package uk.gov.dhsc.htbhf.smartstub.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Generates household identifiers for both HMRC and DWP. They have been made such that they are both based
 * from the NINO provided but will yield different identifiers for DWP and HMRC for the same NINO.
 */
@Service
public class IdentifierService {

    public static final String DWP_DELIMITER = "-";
    public static final String HMRC_DELIMITER = "_";

    private final Base64.Encoder encoder = Base64.getEncoder();

    public String getDWPHouseholdIdentifier(String nino) {
        return encodeIdentifier(nino + DWP_DELIMITER);
    }

    public String getHMRCHouseholdIdentifier(String nino) {
        return encodeIdentifier(nino + HMRC_DELIMITER);
    }

    private String encodeIdentifier(String valueToEncode) {
        return encoder.encodeToString(valueToEncode.getBytes());
    }
}
