package uk.gov.dhsc.htbhf.smartstub.controller.v2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.smartstub.model.v2.IdentityAndEligibilityResponse;

import java.util.Map;

@RestController
@RequestMapping("/v2/dwp/benefits")
@Slf4j
@AllArgsConstructor
public class DWPBenefitControllerV2 {

    @GetMapping
    public IdentityAndEligibilityResponse determineEligibility(@RequestHeader Map<String, String> headers) {
        log.debug("Received DWP eligibility request, headers: {}", headers);
        return IdentityAndEligibilityResponse.builder().build();
    }

}
