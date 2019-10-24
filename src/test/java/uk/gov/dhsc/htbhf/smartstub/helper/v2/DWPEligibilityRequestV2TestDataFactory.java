package uk.gov.dhsc.htbhf.smartstub.helper.v2;

import uk.gov.dhsc.htbhf.smartstub.model.v2.DWPEligibilityRequestV2;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.UC_MONTHLY_INCOME_THRESHOLD;
import static uk.gov.dhsc.htbhf.smartstub.helper.v2.PersonDTOV2TestDataFactory.aValidPersonDTOV2;

public class DWPEligibilityRequestV2TestDataFactory {

    public static DWPEligibilityRequestV2 aValidDWPEligibilityRequestV2() {
        return DWPEligibilityRequestV2.builder()
                .person(aValidPersonDTOV2())
                .eligibleEndDate(LocalDate.now().plusDays(28))
                .ucMonthlyIncomeThresholdInPence(UC_MONTHLY_INCOME_THRESHOLD)
                .build();
    }

}
