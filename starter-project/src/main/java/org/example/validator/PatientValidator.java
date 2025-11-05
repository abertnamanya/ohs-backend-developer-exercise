package org.example.validator;

import org.example.Patient;
import org.example.Utils.UtilsHelper.Gender;
import org.example.Utils.UtilsHelper.PatientIdentifiersType;
import org.example.requests.PatientRequest;
import org.example.service.PatientService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.format.DateTimeFormatter;

@Component
public class PatientValidator implements Validator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final PatientService patientService;

    public PatientValidator(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Patient.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, null);
    }

    public void validate(Object target, Errors errors, Long patientId) {

        PatientRequest request = (PatientRequest) target;


//        if (request.getBirthDate() != null) {
//            try {
//                LocalDate.parse(request.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            } catch (DateTimeParseException e) {
//                errors.rejectValue("birthDateString", "dateOfBirth.format", "Date of birth must be in yyyy-MM-dd format");
//            }
//        }

//        if (request.getBirthDate() != null && request.getBirthDate().isAfter(LocalDate.now())) {
//            errors.rejectValue("dateOfBirth", "dateOfBirth.future", "Date of birth cannot be in the future");
//        }
        String identifier = patientService.checkIfIdentifierAlreadyExists(request.getPatientIdentifier()).getPatientIdentifier();

        if (identifier != null && patientId == null) {
            errors.rejectValue("patientIdentifier", "patientIdentifier.duplicate",
                    "Patient identifier already exists");
        }

        if (request.getPatientIdentifierType() == null || request.getPatientIdentifier().trim().isEmpty()) {
            errors.rejectValue("patientIdentifierType", "patientIdentifierType.empty", "PatientIdentifierType is required and must be either driving permit, national id, work id, or other");
        } else {
            try {
                PatientIdentifiersType.valueOf(request.getPatientIdentifierType().toUpperCase());
            } catch (IllegalArgumentException ex) {
                errors.rejectValue("patientIdentifierType", "patientIdentifierType.invalid", "PatientIdentifierType must be either driving permit, national id, work id, or other ");
            }
        }

        if (request.getGender() == null || request.getGender().trim().isEmpty()) {
            errors.rejectValue("gender", "gender.empty", "Gender is required and must be male, female, or other");
        } else {
            try {
                Gender.valueOf(request.getGender().toUpperCase());
            } catch (IllegalArgumentException ex) {
                errors.rejectValue("gender", "gender.invalid", "Gender must be male, female, or other");
            }
        }
    }
}
