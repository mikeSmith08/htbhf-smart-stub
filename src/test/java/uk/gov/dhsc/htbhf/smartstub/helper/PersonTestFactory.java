package uk.gov.dhsc.htbhf.smartstub.helper;

import uk.gov.dhsc.htbhf.smartstub.model.AddressDTO;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import java.time.LocalDate;

/**
 * Test data factory for {@link PersonDTO} objects.
 */
public class PersonTestFactory {

    private static final LocalDate DOB = LocalDate.parse("1985-12-31");
    private static final String ADDRESS_LINE_1 = "Flat b";
    private static final String ADDRESS_LINE_2 = "123 Fake street";
    private static final String TOWN_OR_CITY = "Springfield";
    private static final String POSTCODE = "AA1 1AA";

    /**
     * Creates a {@link PersonDTO} request object with a nino that encodes to a person with no benefits or children.
     */
    public static PersonDTO aPersonWithNoBenefitsAndNoChildren() {
        final String nino = "BA000056C";
        return PersonDTO.builder()
                .dateOfBirth(DOB)
                .nino(nino)
                .address(aValidAddress())
                .build();
    }

    private static AddressDTO aValidAddress() {
        return AddressDTO.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .townOrCity(TOWN_OR_CITY)
                .postcode(POSTCODE)
                .build();
    }
}
