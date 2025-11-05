package org.example.mapper;

import org.example.Patient;
import org.example.Utils.UtilsHelper.Gender;
import org.example.Utils.UtilsHelper.PatientIdentifiersType;
import org.example.requests.PatientRequest;

public class PatientMapper {

    public static PatientRequest toDto(Patient patient) {
        PatientRequest dto = new PatientRequest();
        if (patient != null) {
            dto.setPatientId(patient.getId());
            dto.setPatientIdentifier(patient.getPatientIdentifier());
            dto.setPatientIdentifierType(patient.getPatientIdentifierType().toString());
            dto.setFamilyName(patient.getFamilyName());
            dto.setGivenName(patient.getGivenName());
            dto.setGender(patient.getGender().toString());
            dto.setBirthDate(patient.getDateOfBirth());
        }
        return dto;
    }

    public static Patient toEntity(PatientRequest dto) {
        Patient entity = new Patient();
        entity.setPatientIdentifier(dto.getPatientIdentifier());

        entity.setPatientIdentifier(dto.getPatientIdentifier());
        entity.setPatientIdentifierType(PatientIdentifiersType.fromString(dto.getPatientIdentifierType()));
        entity.setFamilyName(dto.getFamilyName());
        entity.setGivenName(dto.getGivenName());
        entity.setGender(Gender.fromString(dto.getGender()));
        entity.setDateOfBirth(dto.getBirthDate());

        return entity;
    }
}
