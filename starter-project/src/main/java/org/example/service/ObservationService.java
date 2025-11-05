package org.example.service;

import org.example.Observation;
import org.example.Utils.ResourceNotFoundException;
import org.example.repository.ObservationRepository;
import org.example.requests.ObsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    public Observation createObs(Observation observation) {
        return observationRepository.save(observation);
    }

    public List<ObsRequest> fetchObservationsByPatientId(Long patientId) {
        List<ObsRequest> observations = observationRepository.findByEncounterPatientId(patientId).stream()
                .map(obs -> {
                    ObsRequest dto = new ObsRequest();
                    dto.setObservationId(obs.getId());
                    dto.setCode(obs.getCode());
                    dto.setValue(obs.getValue());
                    dto.setEffectiveDateTime(obs.getEffectiveDateTime());
                    return dto;
                }).toList();

        if (observations.isEmpty()) {
            throw new ResourceNotFoundException("No patient observations found with patient Id: " + patientId);
        }

        return observations;
    }
}
