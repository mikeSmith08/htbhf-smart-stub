package uk.gov.dhsc.htbhf.smartstub.helper.v2;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.*;

public class HttpRequestTestDataFactory {

    public static HttpEntity<Void> aValidEligibilityHttpEntity() {
        LocalDate eligibilityEndDate = LocalDate.now().plusDays(28);
        String eligibilityEndDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(eligibilityEndDate);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("surname", SIMPSON_LAST_NAME);
        httpHeaders.add("nino", NINO);
        httpHeaders.add("dateOfBirth", HOMER_DATE_OF_BIRTH_STRING);
        httpHeaders.add("eligibleEndDate", eligibilityEndDateString);
        httpHeaders.add("addressLine1", SIMPSONS_ADDRESS_LINE_1);
        httpHeaders.add("postcode", SIMPSONS_POSTCODE);
        httpHeaders.add("emailAddress", HOMER_EMAIL);
        httpHeaders.add("mobilePhoneNumber", HOMER_MOBILE);
        httpHeaders.add("pregnantDependentDob", MAGGIE_DATE_OF_BIRTH_STRING);
        httpHeaders.add("ucMonthlyIncomeThreshold", String.valueOf(UC_MONTHLY_INCOME_THRESHOLD));
        return new HttpEntity<>(httpHeaders);
    }
}
