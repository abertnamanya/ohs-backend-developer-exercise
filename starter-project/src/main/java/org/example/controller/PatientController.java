package org.example.controller;

import jakarta.validation.Valid;
import org.example.requests.ApiResponseHandler;
import org.example.requests.EncounterRequest;
import org.example.requests.ObsRequest;
import org.example.requests.PatientRequest;
import org.example.service.ObservationService;
import org.example.service.PatientService;
import org.example.validator.PatientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientValidator patientValidator;

    @Autowired
    private ObservationService observationService;

    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientRequest request) {

        Errors errors = new BeanPropertyBindingResult(request, "patientRequest");
        patientValidator.validate(request, errors);

        if (errors.hasErrors()) {
            var fieldErrors = errors.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            err -> err.getField(),
                            err -> err.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(fieldErrors);
        }

        return ResponseEntity.ok(ApiResponseHandler.of("Patient created successfully", patientService.savePatient(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientRequest> getPatientById(@PathVariable Long id) {
        PatientRequest patient = patientService.findByPatientId(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public List<PatientRequest> fetchAllPatients(
            @RequestParam Map<String, String> params
    ) {
        if (!Set.of("family", "given", "identifier", "birthDate").containsAll(params.keySet())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown search parameter");
        }
        if (params.isEmpty()) {
            return patientService.getAllActivePatients();
        }
        return patientService.searchPatientByParams(
                params.get("family"),
                params.get("given"),
                params.get("identifier"),
                params.containsKey("birthDate") ? LocalDate.parse(params.get("birthDate")) : null
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@Valid @RequestBody PatientRequest request,
                                           @PathVariable Long id) {

        Errors errors = new BeanPropertyBindingResult(request, "patientRequest");
        patientValidator.validate(request, errors,
                id);

        if (errors.hasErrors()) {
            var fieldErrors = errors.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            err -> err.getField(),
                            err -> err.getDefaultMessage()
                    ));
            return ResponseEntity.badRequest().body(fieldErrors);
        }

        return ResponseEntity.ok(ApiResponseHandler.of("Patient updated successfully", patientService.updatePatient(request, id)));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponseHandler.of("Patient has been deleted successfully"));
    }

    @GetMapping("/{id}/encounters")
    public List<EncounterRequest> getPatientEncounters(@PathVariable Long id) {
        return patientService.getEncountersForPatient(id);
    }


    @GetMapping("/{id}/observations")
    public ResponseEntity<?> getObservationsByPatient(@PathVariable Long id) {
        List<ObsRequest> observations = observationService.fetchObservationsByPatientId(id);
        return ResponseEntity.ok(ApiResponseHandler.of(observations));
    }


}
