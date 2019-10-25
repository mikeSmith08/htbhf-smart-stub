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

    /**
     * Determines the eligibility of the claimant from the given request details. The request
     * object is built from header values in the request using {@link DwpEligibilityRequestResolver}.
     * The request object is validated, but due to having to use the argument resolver, this
     * is done manually in {@link DwpEligibilityRequestResolver} rather than using Spring's @Valid
     * annotation because this doesn't work on request parameters which are built using
     * an argument resolver.
     *
     * @param request The valid request object built up by {@link DwpEligibilityRequestResolver}
     * @return The identity and eligibility response.
     */
    @GetMapping
    public IdentityAndEligibilityResponse determineEligibility(DWPEligibilityRequestV2 request) {
        log.debug("Received DWP eligibility request: {}", request);
        return IdentityAndEligibilityResponse.builder().build();
    }

}
