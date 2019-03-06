package uk.gov.dhsc.htbhf.smartstub.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.smartstub.model.BenefitDTO;
import uk.gov.dhsc.htbhf.smartstub.model.PersonDTO;

import static java.util.Collections.emptyList;

@RestController
public class DWPBenefitController {

    @PostMapping
    public BenefitDTO getBenefits(@RequestBody PersonDTO person) {
        return BenefitDTO.builder()
                .benefits(emptyList())
                .numberOfChildrenUnderOne(0)
                .numberOfChildrenUnderFour(0)
                .build();
    }
}
