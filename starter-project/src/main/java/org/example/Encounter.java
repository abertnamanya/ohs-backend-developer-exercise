package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.example.Utils.UtilsHelper.EncounterClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time")
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EncounterClass encounterClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private boolean voided = false;


    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Observation> obs = new ArrayList<>();

    public Encounter() {
    }

    public Encounter(Patient patient, LocalDateTime start, LocalDateTime end, EncounterClass encounterClass) {
        this.patient = patient;
        this.start = start;
        this.end = end;
        this.encounterClass = encounterClass;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public EncounterClass getEncounterClass() {
        return encounterClass;
    }

    public void setEncounterClass(EncounterClass encounterClass) {
        this.encounterClass = encounterClass;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void addObservation(Observation observation) {
        if (this.obs == null) {
            this.obs = new ArrayList<>();
        }
        observation.setEncounter(this); // link both ways if Observation has `encounter` field
        this.obs.add(observation);
    }

    public List<Observation> getObs() {
        return obs;
    }

    public void setObs(List<Observation> obs) {
        this.obs = obs;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }
}
