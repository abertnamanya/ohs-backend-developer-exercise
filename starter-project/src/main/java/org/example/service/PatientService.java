package org.example.service;

import org.example.Patient;
import org.example.requests.EncounterRequest;
import org.example.requests.PatientRequest;

import java.time.LocalDate;
import java.util.List;


public interface PatientService {


    PatientRequest findByPatientId(Long patientId);

    Patient findByPatientIdReturnPatient(Long patientId);

    PatientRequest savePatient(PatientRequest patient);

    PatientRequest updatePatient(PatientRequest patient, Long patientId);

    List<PatientRequest> getAllActivePatients();

    PatientRequest findPatientByIdentifier(String patientIdentifier);

    PatientRequest checkIfIdentifierAlreadyExists(String patientIdentifier);

    List<PatientRequest> searchPatientByParams(String family, String given, String identifier, LocalDate birthDate);

    void deletePatient(Long id);


    List<EncounterRequest> getEncountersForPatient(Long patientId);

}