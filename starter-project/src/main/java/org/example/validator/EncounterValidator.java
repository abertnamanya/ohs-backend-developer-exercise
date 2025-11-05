package org.example.validator;

import org.example.requests.EncounterRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EncounterValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return EncounterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EncounterRequest request = (EncounterRequest) target;

//        if (!request.getObs().isEmpty()) {
//            for (Observation obs : request.getObs()) {
//                if (obs.getValue() == null) {
//                    errors.rejectValue("obsValue", null, "Obs value is required");
//                }
//                if (obs.getCode() == null) {
//                    errors.rejectValue("code", null, "Code is required");
//                }
//            }
//
//        }

    }


}
