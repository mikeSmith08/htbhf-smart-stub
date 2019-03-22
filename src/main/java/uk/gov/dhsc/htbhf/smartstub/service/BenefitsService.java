package uk.gov.dhsc.htbhf.smartstub.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus;

import java.util.Map;

import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.NOMATCH;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.PENDING;

/**
 * Service for creating a {@link BenefitDTO} response based on a given national insurance number (nino).
 * See README.md for details on mappings.
 */
@Service
@AllArgsConstructor
@Slf4j
public class BenefitsService {

    public static final String EXCEPTIONAL_NINO = "ZZ999999D";
    private static final int DWP_ELIGIBILITY_STATUS_POSITION = 0;
    private static final int HMRC_ELIGIBILITY_STATUS_POSITION = 1;
    private static final int CHILDREN_UNDER_ONE_POSITION = 2;
    private static final int CHILDREN_UNDER_FOUR_POSITION = 3;
    private static final Map<Character, EligibilityStatus> ELIGIBILITY_STATUS_MAP = Map.of('E', ELIGIBLE, 'I', INELIGIBLE, 'P', PENDING);

    private final IdentifierService identifierService;

    public BenefitDTO getDWPBenefits(String nino) {
        String householdIdentifier = identifierService.getDWPHouseholdIdentifier(nino);
        return getBenefits(nino, DWP_ELIGIBILITY_STATUS_POSITION, householdIdentifier);
    }

    public BenefitDTO getHMRCBenefits(String nino) {
        String householdIdentifier = identifierService.getHMRCHouseholdIdentifier(nino);
        return getBenefits(nino, HMRC_ELIGIBILITY_STATUS_POSITION, householdIdentifier);
    }

    private BenefitDTO getBenefits(String nino, int eligibilityStatusPosition, String householdIdentifier) {
        if (EXCEPTIONAL_NINO.equals(nino)) {
            String message = "NINO provided (" + EXCEPTIONAL_NINO + ") has been configured to trigger an Exception";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        char[] ninoChars = nino.toCharArray();
        var status = ELIGIBILITY_STATUS_MAP.getOrDefault(ninoChars[eligibilityStatusPosition], NOMATCH);
        if (status == NOMATCH) {
            return BenefitDTO.builder().eligibilityStatus(NOMATCH).build();
        }

        Integer childrenUnderFour = getNumberOfChildrenUnderFour(ninoChars);
        Integer childrenUnderOne = getNumberOfChildrenUnderOne(childrenUnderFour, ninoChars);

        return BenefitDTO.builder()
                .eligibilityStatus(status)
                .numberOfChildrenUnderOne(childrenUnderOne)
                .numberOfChildrenUnderFour(childrenUnderFour)
                .householdIdentifier(householdIdentifier)
                .build();
    }

    private Integer getNumberOfChildrenUnderOne(Integer childrenUnderFour, char[] nino) {
        Integer childrenUnderOne = Character.getNumericValue(nino[CHILDREN_UNDER_ONE_POSITION]);
        return (childrenUnderOne > childrenUnderFour) ? childrenUnderFour : childrenUnderOne;
    }

    private Integer getNumberOfChildrenUnderFour(char[] nino) {
        return Character.getNumericValue(nino[CHILDREN_UNDER_FOUR_POSITION]);
    }
}
