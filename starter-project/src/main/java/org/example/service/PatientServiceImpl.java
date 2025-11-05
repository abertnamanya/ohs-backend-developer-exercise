package org.example.service;

import org.example.Encounter;
import org.example.Patient;
import org.example.Utils.ResourceNotFoundException;
import org.example.Utils.UtilsHelper.Gender;
import org.example.Utils.UtilsHelper.PatientIdentifiersType;
import org.example.mapper.EncounterMapper;
import org.example.mapper.PatientMapper;
import org.example.repository.EncounterRepository;
import org.example.repository.PatientRepository;
import org.example.requests.EncounterRequest;
import org.example.requests.PatientRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    private final EncounterRepository encounterRepository;

    public PatientServiceImpl(PatientRepository patientRepository, EncounterRepository encounterRepository) {
        this.patientRepository = patientRepository;
        this.encounterRepository = encounterRepository;
    }

    @Override
    public PatientRequest findByPatientId(Long patientId) {
        Patient patient = patientRepository.findPatientById(patientId);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return PatientMapper.toDto(patient);
    }

    @Override
    public Patient findByPatientIdReturnPatient(Long patientId) {
        Patient patient = patientRepository.findPatientById(patientId);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return patient;
    }

    @Override
    public PatientRequest savePatient(PatientRequest request) {
        String identifier = request.getPatientIdentifier();
        Patient patient = patientRepository.findPatientByPatientIdentifier(identifier);
        if (patient != null) {
            throw new IllegalArgumentException("Patient with identifier " + identifier + " already exists");
        }
        Patient createPatient = PatientMapper.toEntity(request);
        if (patientRepository.save(createPatient).getId() == null) {
            throw new RuntimeException("Failed to save patient");
        }
        return PatientMapper.toDto(createPatient);
    }

    @Override
    public PatientRequest updatePatient(PatientRequest request, Long patientId) {
        Patient patient = patientRepository.findPatientById(patientId);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        patient.setGivenName(request.getGivenName());
        patient.setFamilyName(request.getFamilyName());
        patient.setGender(Gender.fromString(request.getGender()));
        patient.setDateOfBirth(request.getBirthDate());
        patient.setPatientIdentifier(request.getPatientIdentifier());
        patient.setPatientIdentifierType(PatientIdentifiersType.fromString(request.getPatientIdentifierType()));

        if (patientRepository.save(patient).getId() == null) {
            throw new RuntimeException("Failed to update patient");
        }
        return PatientMapper.toDto(patient);
    }

    @Override
    public List<PatientRequest> getAllActivePatients() {
        List<Patient> patients = patientRepository.findByVoidedFalse();
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("No active patients found");
        }
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    @Override
    public PatientRequest findPatientByIdentifier(String patientIdentifier) {
        Patient patient = patientRepository.findPatientByPatientIdentifier(patientIdentifier);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient with identifier " + patientIdentifier + " not found");
        }
        return PatientMapper.toDto(patient);
    }

    @Override
    public PatientRequest checkIfIdentifierAlreadyExists(String patientIdentifier) {
        Patient patient = patientRepository.findPatientByPatientIdentifier(patientIdentifier);
        return PatientMapper.toDto(patient);
    }


    @Override
    public List<PatientRequest> searchPatientByParams(String family, String given, String identifier, LocalDate birthDate) {
        List<Patient> patients = patientRepository.filterPatientByParams(family, given, identifier, birthDate);
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("No patient found with the specified parameters");
        }
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    @Override
    public void deletePatient(Long patientId) {
        Patient patient = patientRepository.findPatientById(patientId);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        patient.setVoided(true);
        patientRepository.save(patient);
    }

    @Override
    public List<EncounterRequest> getEncountersForPatient(Long patientId) {
        Patient patient = patientRepository.findPatientById(patientId);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        List<Encounter> encounters = encounterRepository.findByPatientId(patientId);
        if (encounters.isEmpty()) {
            throw new ResourceNotFoundException("No encounters found for this patient with id: " + patientId);
        }
        return encounters.stream().map(EncounterMapper::toDto).toList();
    }
}
