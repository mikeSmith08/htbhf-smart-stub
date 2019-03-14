package uk.gov.dhsc.htbhf.smartstub.helper;

import uk.gov.dhsc.htbhf.smartstub.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.smartstub.helper.PersonTestFactory.aPersonWhoIsEligible;

public class EligibilityRequestTestDataFactory {

    private static final LocalDate ELIGIBLE_END_DATE = LocalDate.parse("2019-03-01");
    private static final LocalDate ELIGIBLE_START_DATE = LocalDate.parse("2019-02-14");
    private static final BigDecimal UC_MONTHLY_INCOME_THRESHOLD = BigDecimal.valueOf(408);

    public static EligibilityRequest anEligibilityRequest() {
        return EligibilityRequest.builder()
                .person(aPersonWhoIsEligible())
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE)
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD)
                .build();
    }

    public static EligibilityRequest anEligibilityRequestWithPerson(PersonDTO person) {
        return EligibilityRequest.builder()
                .person(person)
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE)
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD)
                .build();
    }
}
