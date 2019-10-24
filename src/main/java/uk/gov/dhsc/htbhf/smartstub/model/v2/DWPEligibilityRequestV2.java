package uk.gov.dhsc.htbhf.smartstub.model.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class DWPEligibilityRequestV2 {

    @NotNull
    @Valid
    @JsonProperty("person")
    private PersonDTOV2 person;

    @NotNull
    @JsonProperty("ucMonthlyIncomeThreshold")
    private final Integer ucMonthlyIncomeThresholdInPence;

    @NotNull
    @JsonProperty("eligibleEndDate")
    private final LocalDate eligibleEndDate;
}
