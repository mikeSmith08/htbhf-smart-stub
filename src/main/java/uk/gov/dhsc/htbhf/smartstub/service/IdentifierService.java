package uk.gov.dhsc.htbhf.smartstub.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class IdentifierService {

    private final Base64.Encoder encoder = Base64.getEncoder();

    public String getHouseholdIdentifier(String nino) {
        return encoder.encodeToString((nino + "-").getBytes());
    }
}
