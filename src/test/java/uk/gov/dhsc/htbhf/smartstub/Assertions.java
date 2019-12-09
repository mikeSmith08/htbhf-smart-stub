package uk.gov.dhsc.htbhf.smartstub;

import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.v2.VerificationOutcome;

import static org.assertj.core.api.Assertions.assertThat;

public class Assertions {

    public static void assertIsEqualIgnoringHouseholdIdentifier(IdentityAndEligibilityResponse actual, IdentityAndEligibilityResponse expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "householdIdentifier");
        if (expected.isEligible()) {
            assertThat(actual.getHouseholdIdentifier()).isNotBlank();
        } else {
            assertThat(actual.getHouseholdIdentifier()).isEmpty();
        }
    }

}
