package uk.gov.dhsc.htbhf.smartstub.converter.v2;

import org.springframework.web.context.request.NativeWebRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import static uk.gov.dhsc.htbhf.smartstub.converter.v2.ConverterUtils.nullSafeGetDate;
import static uk.gov.dhsc.htbhf.smartstub.converter.v2.ConverterUtils.nullSafeGetInteger;

/**
 * Converts the HTTP headers in the web request given as a part of the DWP Eligibility Request into a DTO object.
 */
public class RequestHeaderToDWPEligibilityRequestConverter {

    public DWPEligibilityRequest convert(NativeWebRequest webRequest) {
        return DWPEligibilityRequest.builder()
                .person(buildPerson(webRequest))
                .eligibilityEndDate(nullSafeGetDate(webRequest, "eligibilityEndDate"))
                .ucMonthlyIncomeThresholdInPence(nullSafeGetInteger(webRequest, "ucMonthlyIncomeThreshold"))
                .build();
    }

    private PersonDTO buildPerson(NativeWebRequest webRequest) {
        return PersonDTO.builder()
                .surname(webRequest.getHeader("surname"))
                .nino(webRequest.getHeader("nino"))
                .dateOfBirth(nullSafeGetDate(webRequest, "dateOfBirth"))
                .addressLine1(webRequest.getHeader("addressLine1"))
                .postcode(webRequest.getHeader("postcode"))
                .emailAddress(webRequest.getHeader("emailAddress"))
                .mobilePhoneNumber(webRequest.getHeader("mobilePhoneNumber"))
                .pregnantDependentDob(nullSafeGetDate(webRequest, "pregnantDependentDob"))
                .build();
    }

}
