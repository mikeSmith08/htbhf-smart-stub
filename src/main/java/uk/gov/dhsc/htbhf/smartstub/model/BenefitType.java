package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum BenefitType {

    UNIVERSAL_CREDIT("Universal Credit"),
    CHILD_TAX_CREDITS("Child Tax Credits");

    @Getter
    private final String value;
}
