package org.example.service;

import org.example.Encounter;
import org.example.requests.EncounterRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EncounterService {


    List<EncounterRequest> getEncountersByPatientId(Long patientId);

    EncounterRequest createEncounter(EncounterRequest encounter);

    Encounter findEncounterById(Long encounterId);

    EncounterRequest findEncounterByIdReturnEncounterRequest(Long encounterId);

    EncounterRequest updateEncounter(EncounterRequest request, Long id);

    void deletePatient(Long id);

    List<EncounterRequest> getAllNonVoidedEncounters();

    List<EncounterRequest> searchEncountersByParams(String encounterClass, LocalDateTime startDate, LocalDateTime endDate);
}
