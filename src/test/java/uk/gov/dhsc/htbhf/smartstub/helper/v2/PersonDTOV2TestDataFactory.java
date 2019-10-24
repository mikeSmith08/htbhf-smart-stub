package uk.gov.dhsc.htbhf.smartstub.helper.v2;

import uk.gov.dhsc.htbhf.smartstub.model.v2.PersonDTOV2;

import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.*;

public class PersonDTOV2TestDataFactory {

    public static PersonDTOV2 aValidPersonDTOV2() {
        return PersonDTOV2.builder()
                .nino(NINO)
                .surname(SIMPSON_LAST_NAME)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .postcode(SIMPSONS_POSTCODE)
                .emailAddress(HOMER_EMAIL)
                .mobilePhoneNumber(HOMER_MOBILE)
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .pregnantDependentDob(MAGGIE_DATE_OF_BIRTH)
                .build();
    }
}
