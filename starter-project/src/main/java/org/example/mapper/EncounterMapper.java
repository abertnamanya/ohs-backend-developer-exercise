package org.example.mapper;

import org.example.Encounter;
import org.example.Patient;
import org.example.Utils.UtilsHelper.EncounterClass;
import org.example.requests.EncounterRequest;

public class EncounterMapper {
    public static EncounterRequest toDto(Encounter encounter) {
        EncounterRequest dto = new EncounterRequest();
        if (encounter != null) {
            dto.setEncounterId(encounter.getId());
            dto.setPatientId(encounter.getPatient().getId());
            dto.setStart(encounter.getStart());
            dto.setEnd(encounter.getEnd());
        }
        return dto;
    }

    public static Encounter toEntity(EncounterRequest dto, Patient patient) {
        Encounter entity = new Encounter();
        entity.setEncounterClass(EncounterClass.fromString(dto.getEncounterClass()));
        entity.setPatient(patient);
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());
        return entity;
    }
}
