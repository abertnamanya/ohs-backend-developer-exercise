package org.example.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ObsRequest {

    private Long observationId;

    @NotBlank(message = "Obs code is required")
    private String code;
    @NotBlank(message = "Obs value is required")
    private String value;
    @NotNull(message = "Effective Date is required")
    private LocalDateTime effectiveDateTime;

    public ObsRequest() {
    }

    public ObsRequest(String code, String value, LocalDateTime effectiveDateTime) {
        this.code = code;
        this.value = value;
        this.effectiveDateTime = effectiveDateTime;
    }

    public Long getObservationId() {
        return observationId;
    }

    public void setObservationId(Long observationId) {
        this.observationId = observationId;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(LocalDateTime effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }
}
