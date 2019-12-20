package uk.gov.dhsc.htbhf.smartstub.service.v2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.model.*;
import uk.gov.dhsc.htbhf.eligibility.model.testhelper.ChildDobGenerator;

import java.util.UUID;

import static java.util.Collections.emptyList;

/**
 * Component responsible for determining the identity and eligibility response for a request based
 * in the NINO and Surname provided.
 */
@Slf4j
@Component
public class IdentityAndEligibilityService {

    public static final String EXCEPTION_NINO = "XX999999D";
    private static final char NOT_MATCHED_CHAR = 'X';
    private static final char ELIGIBILITY_NOT_CONFIRMED_CHAR = 'X';
    private static final int IDENTITY_STATUS_POSITION = 0;
    private static final int ELIGIBILITY_STATUS_POSITION = 1;

    public static final String ADDRESS_LINE_ONE_NOT_MATCHED_SURNAME = "AddressLineOneNotMatched";
    public static final String POSTCODE_NOT_MATCHED_SURNAME = "PostcodeNotMatched";
    public static final String MOBILE_NOT_HELD_SURNAME = "MobileNotHeld";
    public static final String EMAIL_NOT_HELD_SURNAME = "EmailNotHeld";
    public static final String MOBILE_AND_EMAIL_NOT_HELD_SURNAME = "MobileAndEmailNotHeld";
    public static final String MOBILE_NOT_MATCHED_SURNAME = "MobileNotMatched";
    public static final String EMAIL_NOT_MATCHED_SURNAME = "EmailNotMatched";
    public static final String MOBILE_AND_EMAIL_NOT_MATCHED_SURNAME = "MobileAndEmailNotMatched";
    private static final String NO_HOUSEHOLD_IDENTIFIER_PROVIDED = "";

    /**
     * Full details of the rules used to determine the response can be found in the README.md file.
     *
     * @param request The request to evaluate
     * @return The response from the above criteria
     */
    public IdentityAndEligibilityResponse evaluateEligibility(DWPEligibilityRequest request) {

        String nino = request.getPerson().getNino();
        if (EXCEPTION_NINO.equals(nino)) {
            String message = "NINO provided (" + EXCEPTION_NINO + ") has been configured to trigger an Exception";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder = setupDefaultBuilder();
        IdentityOutcome identityStatus = setIdentityStatus(nino, builder);
        EligibilityOutcome eligibilityStatus = setEligibilityStatus(nino, identityStatus, builder);

        if (IdentityOutcome.NOT_MATCHED == identityStatus || EligibilityOutcome.NOT_CONFIRMED == eligibilityStatus) {
            return builder.build();
        }

        String surname = request.getPerson().getSurname();
        setAddressVerificationOutcomes(surname, builder);

        builder.householdIdentifier(UUID.randomUUID().toString());
        builder.qualifyingBenefits(QualifyingBenefits.UNIVERSAL_CREDIT);
        setEmailAndMobileVerificationOutcomes(builder, request.getPerson());
        setDobOfChildrenUnder4(builder, nino);
        setPregnantChildDOBMatch(builder, request.getPerson());
        return builder.build();
    }

    private void setPregnantChildDOBMatch(IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder, PersonDTO person) {
        if (person.getPregnantDependentDob() == null) {
            builder.pregnantChildDOBMatch(VerificationOutcome.NOT_SUPPLIED);
        }
    }

    private void setDobOfChildrenUnder4(IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder, String nino) {
        builder.dobOfChildrenUnder4(ChildDobGenerator.createDatesOfBirthForChildren(nino));
    }

    private void setEmailAndMobileVerificationOutcomes(IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder, PersonDTO person) {
        VerificationOutcomeForSurname verificationOutcomeForSurname = VerificationOutcomeForSurname.getVerificationOutcomesForSurname(person.getSurname());
        VerificationOutcome mobileVerificationOutcome = StringUtils.isEmpty(person.getMobilePhoneNumber())
                ? VerificationOutcome.NOT_SUPPLIED : verificationOutcomeForSurname.getMobileOutcome();
        builder.mobilePhoneMatch(mobileVerificationOutcome);
        VerificationOutcome emailVerificationOutcome = StringUtils.isEmpty(person.getEmailAddress())
                ? VerificationOutcome.NOT_SUPPLIED : verificationOutcomeForSurname.getEmailOutcome();
        builder.emailAddressMatch(emailVerificationOutcome);
    }

    private void setAddressVerificationOutcomes(String surname, IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        if (isAddressLine1NotMatchedSurname(surname)) {
            builder.addressLine1Match(VerificationOutcome.NOT_MATCHED);
            builder.postcodeMatch(VerificationOutcome.MATCHED);
        } else if (isPostcodeNotMatchedSurname(surname)) {
            builder.addressLine1Match(VerificationOutcome.MATCHED);
            builder.postcodeMatch(VerificationOutcome.NOT_MATCHED);
        } else {
            builder.addressLine1Match(VerificationOutcome.MATCHED);
            builder.postcodeMatch(VerificationOutcome.MATCHED);
        }
    }

    private boolean isAddressLine1NotMatchedSurname(String surname) {
        return ADDRESS_LINE_ONE_NOT_MATCHED_SURNAME.equals(surname);
    }

    private boolean isPostcodeNotMatchedSurname(String surname) {
        return POSTCODE_NOT_MATCHED_SURNAME.equals(surname);
    }

    private EligibilityOutcome setEligibilityStatus(String nino, IdentityOutcome identityStatus,
                                                    IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        EligibilityOutcome eligibilityStatus = determineEligibilityStatus(identityStatus, nino);
        builder.eligibilityStatus(eligibilityStatus);
        return eligibilityStatus;
    }

    private EligibilityOutcome determineEligibilityStatus(IdentityOutcome identityStatus, String nino) {
        if (identityStatus == IdentityOutcome.NOT_MATCHED) {
            return EligibilityOutcome.NOT_SET;
        }
        return (nino.charAt(ELIGIBILITY_STATUS_POSITION) == ELIGIBILITY_NOT_CONFIRMED_CHAR)
                ? EligibilityOutcome.NOT_CONFIRMED : EligibilityOutcome.CONFIRMED;
    }

    private IdentityOutcome setIdentityStatus(String nino, IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        IdentityOutcome identityStatus = (nino.charAt(IDENTITY_STATUS_POSITION) == NOT_MATCHED_CHAR)
                ? IdentityOutcome.NOT_MATCHED : IdentityOutcome.MATCHED;
        builder.identityStatus(identityStatus);
        return identityStatus;
    }

    private IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder setupDefaultBuilder() {
        return IdentityAndEligibilityResponse.builder()
                .addressLine1Match(VerificationOutcome.NOT_SET)
                .postcodeMatch(VerificationOutcome.NOT_SET)
                .mobilePhoneMatch(VerificationOutcome.NOT_SET)
                .emailAddressMatch(VerificationOutcome.NOT_SET)
                .qualifyingBenefits(QualifyingBenefits.NOT_SET)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .dobOfChildrenUnder4(emptyList())
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .deathVerificationFlag(DeathVerificationFlag.N_A);
    }
}
