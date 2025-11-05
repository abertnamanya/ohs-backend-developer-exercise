package org.example.service;


import org.example.Encounter;
import org.example.Patient;
import org.example.Utils.ResourceNotFoundException;
import org.example.mapper.EncounterMapper;
import org.example.repository.EncounterRepository;
import org.example.repository.PatientRepository;
import org.example.requests.EncounterRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EncounterServiceImpl implements EncounterService {


    private final EncounterRepository encounterRepository;

    private final PatientRepository patientRepository;

    public EncounterServiceImpl(EncounterRepository encounterRepository, PatientRepository patientRepository) {
        this.encounterRepository = encounterRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public EncounterRequest createEncounter(EncounterRequest request) {

        Patient patient = patientRepository.findPatientById(request.getPatientId());
        if (patient == null) {
            throw new IllegalArgumentException("Invalid patient id " + request.getPatientId());
        }
        Encounter encounter = EncounterMapper.toEntity(request, patient);
        if (encounterRepository.save(encounter).getId() == null) {
            throw new RuntimeException("Encounter could not be saved");
        }
        return EncounterMapper.toDto(encounter);
    }

    @Override
    public Encounter findEncounterById(Long encounterId) {
        Encounter encounter = encounterRepository.findEncounterById(encounterId);
        if (encounter == null) {
            throw new IllegalArgumentException("Invalid encounter id " + encounterId);
        }
        return encounter;
    }

    @Override
    public EncounterRequest findEncounterByIdReturnEncounterRequest(Long encounterId) {
        Encounter encounter = encounterRepository.findEncounterById(encounterId);
        if (encounter == null) {
            throw new IllegalArgumentException("Invalid encounter id " + encounterId);
        }
        return EncounterMapper.toDto(encounter);
    }

    @Override
    public EncounterRequest updateEncounter(EncounterRequest request, Long encounterId) {
        Encounter encounter = encounterRepository.findEncounterById(encounterId);
        if (encounter == null) {
            throw new ResourceNotFoundException("Encounter not found with id: " + encounterId);
        }

        Patient patient = patientRepository.findPatientById(request.getPatientId());
        if (patient == null) {
            throw new ResourceNotFoundException("Patient not found with id: " + request.getPatientId());
        }

        encounter.setPatient(patient);
        encounter.setStart(request.getStart());
        encounter.setEnd(request.getStart());
//        encounter.setEncounterClass(request.getEncounterClass().toString());

        if (encounterRepository.save(encounter).getId() == null) {
            throw new RuntimeException("Failed to update encounter with id: " + encounterId);
        }
        return EncounterMapper.toDto(encounter);
    }

    @Override
    public void deletePatient(Long encounterId) {
        Encounter encounter = encounterRepository.findEncounterById(encounterId);
        if (encounter == null) {
            throw new ResourceNotFoundException("Encounter not found with id: " + encounterId);
        }
        encounter.setVoided(true);
        encounterRepository.save(encounter);
    }

    @Override
    public List<EncounterRequest> getAllNonVoidedEncounters() {
        List<Encounter> encounters = encounterRepository.findByVoidedFalse();
        if (encounters.isEmpty()) {
            throw new ResourceNotFoundException("No encounters created yet");
        }
        return encounters.stream().map(EncounterMapper::toDto).toList();
    }


    @Override
    public List<EncounterRequest> searchEncountersByParams(String encounterClass, LocalDateTime startDate, LocalDateTime endDate) {
        List<Encounter> encounters = encounterRepository.filterEncountersByParams(encounterClass, startDate, endDate);
        if (encounters.isEmpty()) {
            throw new ResourceNotFoundException("No encounters found with the specified parameters");
        }
        return encounters.stream().map(EncounterMapper::toDto).toList();
    }

    @Override
    public List<EncounterRequest> getEncountersByPatientId(Long patientId) {
        List<Encounter> encounters = encounterRepository.findByPatientId(patientId);
        return encounters
                .stream()
                .map(EncounterMapper::toDto)
                .toList();
    }


}
