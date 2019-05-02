package uk.gov.dhsc.htbhf.smartstub.helper;

import uk.gov.dhsc.htbhf.smartstub.model.AddressDTO;
import uk.gov.dhsc.htbhf.smartstub.model.CardRequestDTO;

import java.util.UUID;

import static uk.gov.dhsc.htbhf.smartstub.helper.AddressDTOTestDataFactory.aValidAddress;
import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.*;

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
                .firstName(HOMER_FIRST_NAME)
                .lastName(SIMPSON_LAST_NAME)
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .email(HOMER_EMAIL)
                .mobile(HOMER_MOBILE)
                .address(aValidAddress());
    }
}
