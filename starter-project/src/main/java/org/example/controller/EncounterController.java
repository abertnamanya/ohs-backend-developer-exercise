package org.example.controller;

import jakarta.validation.Valid;
import org.example.Encounter;
import org.example.Observation;
import org.example.requests.ApiResponseHandler;
import org.example.requests.EncounterRequest;
import org.example.service.EncounterService;
import org.example.validator.EncounterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encounters")
public class EncounterController {
    @Autowired
    private EncounterService encounterService;


    @Autowired
    private EncounterValidator encounterValidator;

    @GetMapping
    public List<EncounterRequest> getAllEncounters(@RequestParam Map<String, String> params) {
        if (!Set.of("start", "end", "encounterClass").containsAll(params.keySet())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown search parameter");
        }
        if (params.isEmpty()) {
            return encounterService.getAllNonVoidedEncounters();
        }
        return encounterService.searchEncountersByParams(params.get("encounterClass"), params.containsKey("start") ? LocalDateTime.parse(params.get("start")) : null, params.containsKey("end") ? LocalDateTime.parse(params.get("end")) : null);
    }


    @PostMapping
    public ResponseEntity<?> saveEncounter(@RequestBody EncounterRequest request) {

        validateEncounter(request);

        EncounterRequest response = encounterService.createEncounter(request);

        Encounter encounter = encounterService.findEncounterById(response.getEncounterId());

        if (request.getObs() != null && !request.getObs().isEmpty()) {
            request.getObs().forEach(obsReq -> {
                Observation observation = new Observation(encounter, obsReq.getCode(), obsReq.getValue(), obsReq.getEffectiveDateTime());
                encounter.addObservation(observation);
            });

        }

        return ResponseEntity.ok(ApiResponseHandler.of("Encounter created successfully", encounterService.createEncounter(request)));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EncounterRequest> getAllEncounters(@PathVariable Long id) {
        EncounterRequest encounter = encounterService.findEncounterByIdReturnEncounterRequest(id);
        return ResponseEntity.ok(encounter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEncounter(@Valid @RequestBody EncounterRequest request, @PathVariable Long id) {
        validateEncounter(request);
        return ResponseEntity.ok(ApiResponseHandler.of("Encounter has been updated successfully", encounterService.updateEncounter(request, id)));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEncounter(@PathVariable Long id) {
        encounterService.deletePatient(id);
        return ResponseEntity.ok(ApiResponseHandler.of("Encounter has been deleted successfully"));
    }

    private ResponseEntity<Map<String, String>> validateEncounter(EncounterRequest request) {

        Errors errors = new BeanPropertyBindingResult(request, "encounterRequest");
        encounterValidator.validate(request, errors);

        if (errors.hasErrors()) {
            var fieldErrors = errors.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, err -> err.getDefaultMessage()));

            return ResponseEntity.badRequest().body(fieldErrors);
        }

        return null;
    }

}
