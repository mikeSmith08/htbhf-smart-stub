package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class BenefitDTO {

    @JsonProperty("eligibilityStatus")
    private EligibilityStatus eligibilityStatus;

    @JsonProperty("numberOfChildrenUnderOne")
    private final Integer numberOfChildrenUnderOne;

    @JsonProperty("numberOfChildrenUnderFour")
    private final Integer numberOfChildrenUnderFour;

    @JsonProperty("householdIdentifier")
    private final String householdIdentifier;

    @JsonProperty("children")
    private final List<ChildDTO> children;
}
