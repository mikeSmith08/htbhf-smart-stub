package uk.gov.dhsc.htbhf.smartstub.controller.v1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static uk.gov.dhsc.htbhf.smartstub.factory.v1.PostcodeDataFactory.postcodeData;

/**
 * Returns stubbed postcode data matching that returned from api.postcodes.io/postcodes/.
 * See http://postcodes.io/docs
 */
@RestController
@RequestMapping("/v1/postcodes")
@AllArgsConstructor
@Slf4j
public class PostcodesController {

    @GetMapping("/{postcode}")
    public String getPostcodeData(@PathVariable("postcode") String postcode) {
        return postcodeData(postcode);
    }
}
