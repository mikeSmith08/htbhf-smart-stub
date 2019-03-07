package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class BenefitDTO {

    @JsonProperty("benefit")
    private final BenefitType benefit;

    @JsonProperty("numberOfChildrenUnderOne")
    private final Integer numberOfChildrenUnderOne;

    @JsonProperty("numberOfChildrenUnderFour")
    private final Integer numberOfChildrenUnderFour;
}
