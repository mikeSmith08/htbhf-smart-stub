package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class AddressDTO {
    @JsonProperty("addressLine1")
    private String addressLine1;

    @JsonProperty("addressLine2")
    private String addressLine2;

    @JsonProperty("townOrCity")
    private String townOrCity;

    @JsonProperty("postcode")
    private String postcode;
}
