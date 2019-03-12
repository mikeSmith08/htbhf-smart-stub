package uk.gov.dhsc.htbhf.smartstub.service;

import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus;

import java.util.Map;

import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.INELIGIBLE;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.NOMATCH;
import static uk.gov.dhsc.htbhf.smartstub.model.EligibilityStatus.PENDING;

/**
 * Service for creating a {@link BenefitDTO} response based on a given national insurance number (nino.
 * See README.md for details on mappings.
 */
@Service
public class BenefitsService {

    private static final String INVALID_CHILDREN_NUMBER = "Can not have more children under one than children four. Given values were %d, %d";
    private static final int ELIGIBILITY_STATUS_POSITION = 0;
    private static final int CHILDREN_UNDER_ONE_POSITION = 2;
    private static final int CHILDREN_UNDER_FOUR_POSITION = 3;
    private static final Map<Character, EligibilityStatus> ELIGIBILITY_STATUS_MAP = Map.of('E', ELIGIBLE, 'I', INELIGIBLE, 'P', PENDING);

    public BenefitDTO getBenefits(char[] nino) {
        var status = ELIGIBILITY_STATUS_MAP.getOrDefault(nino[ELIGIBILITY_STATUS_POSITION], NOMATCH);
        if (status == NOMATCH) {
            return BenefitDTO.builder().eligibilityStatus(NOMATCH).build();
        }

        var childrenUnderOne = getNumberOfChildrenUnderOne(nino);
        var childrenUnderFour = getNumberOfChildrenUnderFour(nino);

        if(childrenUnderOne > childrenUnderFour) {
            throw new IllegalArgumentException(String.format(INVALID_CHILDREN_NUMBER, childrenUnderOne, childrenUnderFour));
        }

        return BenefitDTO.builder()
                .eligibilityStatus(status)
                .numberOfChildrenUnderOne(childrenUnderOne)
                .numberOfChildrenUnderFour(childrenUnderFour)
                .build();
    }

    private Integer getNumberOfChildrenUnderOne(char[] nino) {
        return Character.getNumericValue(nino[CHILDREN_UNDER_ONE_POSITION]);
    }

    private Integer getNumberOfChildrenUnderFour(char[] nino) {
        return Character.getNumericValue(nino[CHILDREN_UNDER_FOUR_POSITION]);
    }
}
