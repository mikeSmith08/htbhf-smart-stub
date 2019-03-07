package uk.gov.dhsc.htbhf.smartstub.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
class ErrorResponse {
    String message;
}
