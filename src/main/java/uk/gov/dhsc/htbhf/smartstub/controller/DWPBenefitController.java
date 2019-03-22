package uk.gov.dhsc.htbhf.smartstub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.smartstub.service.BenefitsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dwp/benefits")
@Slf4j
public class DWPBenefitController {

    private BenefitsService benefitsService;

    public DWPBenefitController(BenefitsService benefitsService) {
        this.benefitsService = benefitsService;
    }

    @PostMapping
    public BenefitDTO getBenefits(@RequestBody @Valid DWPEligibilityRequest eligibilityRequest) {
        log.debug("Received DWP eligibility request {}", eligibilityRequest);
        BenefitDTO benefits = benefitsService.getDWPBenefits(eligibilityRequest.getPerson().getNino());
        log.debug("Sending DWP response {}", benefits);
        return benefits;
    }
}
