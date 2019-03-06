package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class PersonDTO {

    @JsonProperty("nino")
    private final String nino;

    @JsonProperty("dob")
    private final LocalDate dateOfBirth;

    @JsonProperty("address")
    private final AddressDTO address;
}
