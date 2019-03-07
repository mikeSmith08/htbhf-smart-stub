package uk.gov.dhsc.htbhf.smartstub.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;
import uk.gov.dhsc.htbhf.smartstub.service.BenefitsService;

@RestController
public class DWPBenefitController {

    private BenefitsService benefitsService;

    public DWPBenefitController(BenefitsService benefitsService) {
        this.benefitsService = benefitsService;
    }

    @PostMapping
    public BenefitDTO getBenefits(@RequestBody PersonDTO person) {
        return benefitsService.getBenefits(person.getNino().toCharArray());
    }
}
