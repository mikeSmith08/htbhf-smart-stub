package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class PersonDTO {

    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2}\\d{6}[a-dA-D]")
    @JsonProperty("nino")
    private final String nino;

    @JsonProperty("dob")
    private final LocalDate dateOfBirth;

    @JsonProperty("address")
    private final AddressDTO address;
}
