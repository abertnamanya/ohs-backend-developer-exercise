package org.example.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PatientRequest {

    private Long patientId;

    @NotBlank(message = "Patient identifier is required")
    private String patientIdentifier;

    private String patientIdentifierType;
    @NotBlank(message = "Given name is required")
    private String familyName;
    @NotBlank(message = "Family name is required")
    private String givenName;
    @NotNull(message = "Gender is required and must be MALE, FEMALE, or OTHER")
    private String gender;
    @NotNull(message = "Date of birth is required")
    private LocalDate birthDate;

    public PatientRequest() {
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public String getPatientIdentifierType() {
        return patientIdentifierType;
    }

    public void setPatientIdentifierType(String patientIdentifierType) {
        this.patientIdentifierType = patientIdentifierType;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
