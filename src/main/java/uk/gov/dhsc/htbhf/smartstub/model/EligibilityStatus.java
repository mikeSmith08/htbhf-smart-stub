package uk.gov.dhsc.htbhf.smartstub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EligibilityStatus {

    ELIGIBLE,
    INELIGIBLE,
    PENDING,
    NOMATCH
}
