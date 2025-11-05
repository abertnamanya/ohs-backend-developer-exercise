package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // <--- ADD THIS
    private Encounter encounter;

    @Column(name = "obs_code", nullable = false)
    private String code;

    @Column(name = "obs_value", nullable = false)
    private String value;

    @Column(name = "effective_date_time", nullable = false)
    private LocalDateTime effectiveDateTime;

    @Column(nullable = false)
    private boolean voided = false;


    public Observation() {

    }

    public Observation(Encounter encounter, String code, String value, LocalDateTime effectiveDateTime) {
        this.encounter = encounter;
        this.code = code;
        this.value = value;
        this.effectiveDateTime = effectiveDateTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(LocalDateTime effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }
}
