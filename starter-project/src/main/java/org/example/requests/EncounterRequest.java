package org.example.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class EncounterRequest {

    private Long encounterId;

    @NotBlank(message = "Patient id is required")
    private Long patientId;

    @NotNull(message = "Start Date is required")
    private LocalDateTime start;
    @NotNull(message = "End Date is required")
    private LocalDateTime end;
    @NotNull(message = "Encounter class Date is required")
    private String encounterClass;

    private List<ObsRequest> obs;

    public EncounterRequest() {
    }

    public Long getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Long encounterId) {
        this.encounterId = encounterId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getEncounterClass() {
        return encounterClass;
    }

    public void setEncounterClass(String encounterClass) {
        this.encounterClass = encounterClass;
    }

    public List<ObsRequest> getObs() {
        return obs;
    }

    public void setObs(List<ObsRequest> obs) {
        this.obs = obs;
    }
}
