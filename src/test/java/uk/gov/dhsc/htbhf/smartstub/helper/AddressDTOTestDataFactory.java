package uk.gov.dhsc.htbhf.smartstub.helper;


import uk.gov.dhsc.htbhf.smartstub.model.AddressDTO;

import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.SIMPSONS_ADDRESS_LINE_1;
import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.SIMPSONS_ADDRESS_LINE_2;
import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.SIMPSONS_POSTCODE;
import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.SIMPSONS_TOWN;

public class AddressDTOTestDataFactory {

    public static AddressDTO aValidAddress() {
        return aValidAddressBuilder().build();
    }

    public static AddressDTO anAddressWithAddressLine1(String addressLine1) {
        return aValidAddressBuilder().addressLine1(addressLine1).build();
    }

    private static AddressDTO.AddressDTOBuilder aValidAddressBuilder() {
        return AddressDTO.builder()
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .addressLine2(SIMPSONS_ADDRESS_LINE_2)
                .townOrCity(SIMPSONS_TOWN)
                .postcode(SIMPSONS_POSTCODE);
    }
}
