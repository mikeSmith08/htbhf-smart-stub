package uk.gov.dhsc.htbhf.smartstub.model.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class PersonDTOV2 {

    @NotNull
    @JsonProperty("surname")
    private final String surname;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2}\\d{6}[a-dA-D]")
    @JsonProperty("nino")
    private final String nino;

    @NotNull
    @Past
    @JsonProperty("dateOfBirth")
    private final LocalDate dateOfBirth;

    @NotNull
    @JsonProperty("addressLine1")
    private final String addressLine1;

    @JsonProperty("postcode")
    private final String postcode;

    @JsonProperty("emailAddress")
    private final String emailAddress;

    @JsonProperty("mobilePhoneNumber")
    private final String mobilePhoneNumber;

    @JsonProperty("pregnantDependentDob")
    private LocalDate pregnantDependentDob;
}
