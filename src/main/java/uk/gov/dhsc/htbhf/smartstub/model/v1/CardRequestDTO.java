package uk.gov.dhsc.htbhf.smartstub.model.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class CardRequestDTO {

    @NotNull
    @JsonProperty("firstName")
    private String firstName;

    @NotNull
    @JsonProperty("lastName")
    private String lastName;

    @NotNull
    @Valid
    @JsonProperty("address")
    private AddressDTO address;

    @NotNull
    @Past
    @JsonProperty("dateOfBirth")
    private final LocalDate dateOfBirth;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobile")
    private String mobile;

    @NotNull
    @JsonProperty("claimId")
    private String claimId;
}

