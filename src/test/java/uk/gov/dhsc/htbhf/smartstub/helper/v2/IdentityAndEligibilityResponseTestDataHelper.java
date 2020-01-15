package uk.gov.dhsc.htbhf.smartstub.helper.v2;

import uk.gov.dhsc.htbhf.dwp.model.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome;

public class IdentityAndEligibilityResponseTestDataHelper {

    public static IdentityAndEligibilityResponse addPregnantChildDOBMatch(IdentityAndEligibilityResponse identityAndEligibilityResponse,
                                                                          VerificationOutcome verificationOutcome) {
        //THe builder in common-java has pregnantChildDOBMatch set to NOT_SUPPLIED and no method to override.
        return identityAndEligibilityResponse.toBuilder()
                .pregnantChildDOBMatch(verificationOutcome)
                .build();
    }
}
