package org.example;

import jakarta.persistence.*;
import org.example.Utils.UtilsHelper.Gender;
import org.example.Utils.UtilsHelper.PatientIdentifiersType;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String patientIdentifier;

    @Enumerated(EnumType.STRING)
    private PatientIdentifiersType patientIdentifierType;

    private String givenName;

    private String familyName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private boolean voided = false;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Encounter> encounters;

    public Patient() {
    }

    public Patient(
            String patientIdentifier,
            PatientIdentifiersType patientIdentifierType,
            String givenName, String familyName,
            LocalDate dateOfBirth,
            Gender gender) {
        this.patientIdentifier = patientIdentifier;
        this.patientIdentifierType = patientIdentifierType;
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public PatientIdentifiersType getPatientIdentifierType() {
        return patientIdentifierType;
    }

    public void setPatientIdentifierType(PatientIdentifiersType patientIdentifierType) {
        this.patientIdentifierType = patientIdentifierType;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}