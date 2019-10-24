package uk.gov.dhsc.htbhf.smartstub.model.v2;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VerificationOutcome {
    MATCHED,
    NOT_MATCHED,
    NOT_HELD,
    NOT_SUPPLIED,
    INVALID_FORMAT,
    NOT_SET;

    @JsonValue
    public String getResponseValue() {
        return this.name().toLowerCase();
    }
}
