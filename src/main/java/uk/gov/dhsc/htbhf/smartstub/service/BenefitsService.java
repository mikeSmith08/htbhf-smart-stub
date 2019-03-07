package uk.gov.dhsc.htbhf.smartstub.service;

import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;

import static uk.gov.dhsc.htbhf.smartstub.model.BenefitType.UNIVERSAL_CREDIT;

@Service
public class BenefitsService {

    @SuppressWarnings("FieldCanBeLocal")
    private static int UNIVERSAL_CREDIT_POSITION = 8;

    public BenefitDTO getBenefits(char[] nino) {
        var universalCredit = nino[UNIVERSAL_CREDIT_POSITION] == 'A';
        var benefit = universalCredit ? UNIVERSAL_CREDIT : null;

        return BenefitDTO.builder()
                .benefit(benefit)
                .numberOfChildrenUnderOne(0)
                .numberOfChildrenUnderFour(0)
                .build();
    }
}
