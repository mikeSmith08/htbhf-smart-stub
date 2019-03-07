package uk.gov.dhsc.htbhf.smartstub.service;

import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitType;

import static uk.gov.dhsc.htbhf.smartstub.model.BenefitType.UNIVERSAL_CREDIT;

@Service
public class BenefitsService {

    @SuppressWarnings("FieldCanBeLocal")
    private static final int UNIVERSAL_CREDIT_POSITION = 8;
    private static final int CHILDREN_UNDER_ONE_POSITION = 2;
    private static final int CHILDREN_UNDER_FOUR_POSITION = 3;

    public BenefitDTO getBenefits(char[] nino) {
        var benefit = getBenefit(nino);
        var childrenUnderOne = getNumberOfChildrenUnderOne(nino);
        var childrenUnderFour = getNumberOfChildrenUnderFour(nino);

        return BenefitDTO.builder()
                .benefit(benefit)
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

    private BenefitType getBenefit(char[] nino) {
        var universalCredit = nino[UNIVERSAL_CREDIT_POSITION] == 'A';
        return universalCredit ? UNIVERSAL_CREDIT : null;
    }
}
