package uk.gov.dhsc.htbhf.smartstub.service;

import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.smartstub.exception.PersonNotFoundException;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitType;

import static uk.gov.dhsc.htbhf.smartstub.model.BenefitType.UNIVERSAL_CREDIT;

@Service
public class BenefitsService {

    private static final int PERSON_EXISTS_POSITION = 0;
    private static final int CHILDREN_UNDER_ONE_POSITION = 2;
    private static final int CHILDREN_UNDER_FOUR_POSITION = 3;
    private static final int UNIVERSAL_CREDIT_POSITION = 8;

    public BenefitDTO getBenefits(char[] nino) {
        if (userNotFound(nino)) {
            throw new PersonNotFoundException();
        }

        var benefit = getBenefit(nino);
        var childrenUnderOne = getNumberOfChildrenUnderOne(nino);
        var childrenUnderFour = getNumberOfChildrenUnderFour(nino);

        return BenefitDTO.builder()
                .benefit(benefit)
                .numberOfChildrenUnderOne(childrenUnderOne)
                .numberOfChildrenUnderFour(childrenUnderFour)
                .build();
    }

    private boolean userNotFound(char[] nino) {
        return nino[PERSON_EXISTS_POSITION] == 'A';
    }

    private Integer getNumberOfChildrenUnderOne(char[] nino) {
        return Character.getNumericValue(nino[CHILDREN_UNDER_ONE_POSITION]);
    }

    private Integer getNumberOfChildrenUnderFour(char[] nino) {
        return Character.getNumericValue(nino[CHILDREN_UNDER_FOUR_POSITION]);
    }

    private BenefitType getBenefit(char[] nino) {
        var universalCredit = nino[UNIVERSAL_CREDIT_POSITION] == 'A';
        return universalCredit ? UNIVERSAL_CREDIT : null;
    }
}
