package uk.gov.dhsc.htbhf.smartstub.helper.v1;

import uk.gov.dhsc.htbhf.smartstub.model.v1.AddressDTO;
import uk.gov.dhsc.htbhf.smartstub.model.v1.CardRequestDTO;

import java.util.UUID;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.*;
import static uk.gov.dhsc.htbhf.smartstub.helper.v1.AddressDTOTestDataFactory.aValidAddress;

public class CardRequestDTOTestDataFactory {

    public static CardRequestDTO aValidCardRequest() {
        return defaultCardRequestBuilder().build();
    }

    public static CardRequestDTO aCardRequestWithAddress(AddressDTO address) {
        return defaultCardRequestBuilder()
                .address(address)
                .build();
    }

    public static CardRequestDTO aCardRequestWithFirstName(String firstName) {
        return defaultCardRequestBuilder()
                .firstName(firstName)
                .build();
    }

    public static CardRequestDTO aCardRequestWithLastName(String lastName) {
        return defaultCardRequestBuilder()
                .lastName(lastName)
                .build();
    }

    private static CardRequestDTO.CardRequestDTOBuilder defaultCardRequestBuilder() {
        return CardRequestDTO.builder()
                .claimId(UUID.randomUUID().toString())
                .firstName(HOMER_FORENAME)
                .lastName(SIMPSON_SURNAME)
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .email(HOMER_EMAIL)
                .mobile(HOMER_MOBILE)
                .address(aValidAddress());
    }
}
