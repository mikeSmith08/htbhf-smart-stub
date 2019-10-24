package uk.gov.dhsc.htbhf.smartstub.helper.v2;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.HOMER_DATE_OF_BIRTH_STRING;
import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.NINO;
import static uk.gov.dhsc.htbhf.smartstub.helper.TestConstants.SIMPSON_LAST_NAME;

public class HttpRequestTestDataFactory {

    public static HttpEntity<Void> aValidEligibilityHttpEntity() {
        LocalDate eligibilityEndDate = LocalDate.now().plusDays(28);
        String eligibilityEndDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(eligibilityEndDate);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("surname", SIMPSON_LAST_NAME);
        httpHeaders.add("nino", NINO);
        httpHeaders.add("dateOfBirth", HOMER_DATE_OF_BIRTH_STRING);
        httpHeaders.add("eligibilityEndDate", eligibilityEndDateString);
        return new HttpEntity<>(httpHeaders);
    }
}
