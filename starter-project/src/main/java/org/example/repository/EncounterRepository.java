package org.example.repository;

import org.example.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {


    List<Encounter> findByPatientId(Long patientId);

    List<Encounter> findByVoidedFalse();

    Encounter findEncounterById(Long id);


    @Query("SELECT e FROM Encounter e " + "WHERE (:encounterClass IS NULL OR e.encounterClass = :encounterClass) " + "AND (:start IS NULL OR e.start >= :start) " + "AND (:end IS NULL OR e.start <= :end)")
    List<Encounter> filterEncountersByParams(@Param("encounterClass") String encounterClass, @Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate);
}
