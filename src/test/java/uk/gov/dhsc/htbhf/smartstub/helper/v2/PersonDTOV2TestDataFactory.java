package uk.gov.dhsc.htbhf.smartstub.helper.v2;

import uk.gov.dhsc.htbhf.smartstub.model.v2.PersonDTOV2;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.*;

public class PersonDTOV2TestDataFactory {

    public static PersonDTOV2 aValidPersonDTOV2() {
        return validPersonBuilder().build();
    }

    public static PersonDTOV2 aPersonDTOV2WithSurname(String surname) {
        return validPersonBuilder().surname(surname).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithNino(String nino) {
        return validPersonBuilder().nino(nino).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithSurnameAndNino(String surname, String nino) {
        return validPersonBuilder().surname(surname).nino(nino).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithPostcode(String postcode) {
        return validPersonBuilder().postcode(postcode).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithEmailAddress(String emailAddress) {
        return validPersonBuilder().emailAddress(emailAddress).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithMobilePhoneNumber(String mobilePhoneNumber) {
        return validPersonBuilder().mobilePhoneNumber(mobilePhoneNumber).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithDateOfBirth(LocalDate dateOfBirth) {
        return validPersonBuilder().dateOfBirth(dateOfBirth).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithPregnantDependantDob(LocalDate dateOfBirth) {
        return validPersonBuilder().pregnantDependentDob(dateOfBirth).build();
    }

    public static PersonDTOV2 aPersonDTOV2WithAddressLine1(String addressLine1) {
        return validPersonBuilder().addressLine1(addressLine1).build();
    }

    public static PersonDTOV2 aValidPersonDTOV2WithMandatoryFieldsOnly() {
        return PersonDTOV2.builder()
                .nino(NINO)
                .surname(SIMPSON_LAST_NAME)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .build();
    }

    private static PersonDTOV2.PersonDTOV2Builder validPersonBuilder() {
        return PersonDTOV2.builder()
                .nino(NINO)
                .surname(SIMPSON_LAST_NAME)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .postcode(SIMPSONS_POSTCODE)
                .emailAddress(HOMER_EMAIL)
                .mobilePhoneNumber(HOMER_MOBILE)
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .pregnantDependentDob(MAGGIE_DATE_OF_BIRTH);
    }
}
