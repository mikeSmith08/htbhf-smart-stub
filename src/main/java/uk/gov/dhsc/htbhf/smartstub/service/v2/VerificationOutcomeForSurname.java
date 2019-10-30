package uk.gov.dhsc.htbhf.smartstub.service.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.dhsc.htbhf.smartstub.model.v2.VerificationOutcome;

import java.util.Arrays;

import static uk.gov.dhsc.htbhf.smartstub.model.v2.VerificationOutcome.MATCHED;
import static uk.gov.dhsc.htbhf.smartstub.model.v2.VerificationOutcome.NOT_HELD;
import static uk.gov.dhsc.htbhf.smartstub.model.v2.VerificationOutcome.NOT_MATCHED;
import static uk.gov.dhsc.htbhf.smartstub.service.v2.IdentityAndEligibilityService.*;

@Getter
@AllArgsConstructor
public enum VerificationOutcomeForSurname {

    MOBILE_NOT_HELD(MOBILE_NOT_HELD_SURNAME, NOT_HELD, MATCHED),
    EMAIL_NOT_HELD(EMAIL_NOT_HELD_SURNAME, MATCHED, NOT_HELD),
    MOBILE_AND_EMAIL_NOT_HELD(MOBILE_AND_EMAIL_NOT_HELD_SURNAME, NOT_HELD, NOT_HELD),
    MOBILE_NOT_MATCHED(MOBILE_NOT_MATCHED_SURNAME, NOT_MATCHED, MATCHED),
    EMAIL_NOT_MATCHED(EMAIL_NOT_MATCHED_SURNAME, MATCHED, NOT_MATCHED),
    MOBILE_AND_EMAIL_NOT_MATCHED(MOBILE_AND_EMAIL_NOT_MATCHED_SURNAME, NOT_MATCHED, NOT_MATCHED),
    DEFAULT("", MATCHED, MATCHED);

    private String surname;
    private VerificationOutcome mobileOutcome;
    private VerificationOutcome emailOutcome;

    /**
     * Get the enum value matching the provided surname, or default to a response which has both outcomes being matched.
     *
     * @param surname The surname to match.
     * @return The outcome for both mobile and email.
     */
    public static VerificationOutcomeForSurname getVerificationOutcomesForSurname(String surname) {
        return Arrays.stream(VerificationOutcomeForSurname.values())
                .filter(verificationOutcomeForSurname -> verificationOutcomeForSurname.getSurname().equals(surname))
                .findFirst()
                .orElse(DEFAULT);
    }

}
