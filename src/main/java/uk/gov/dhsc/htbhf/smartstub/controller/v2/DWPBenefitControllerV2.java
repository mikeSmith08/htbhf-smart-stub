package uk.gov.dhsc.htbhf.smartstub.controller.v2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.smartstub.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.smartstub.model.v2.IdentityAndEligibilityResponse;

@RestController
@RequestMapping("/v2/dwp/benefits")
@Slf4j
@AllArgsConstructor
public class DWPBenefitControllerV2 {

    @GetMapping
    public IdentityAndEligibilityResponse determineEligibility(DWPEligibilityRequestV2 request) {
        log.debug("Received DWP eligibility request: {}", request);
        return IdentityAndEligibilityResponse.builder().build();
    }

}
