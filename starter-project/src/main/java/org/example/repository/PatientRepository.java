package org.example.repository;

import org.example.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findPatientById(Long patientId);

//    Patient findPatientByPatientIdentifier(String patientIdentifier);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.patientIdentifier) = LOWER(:identifier)")
    Patient findPatientByPatientIdentifier(@Param("identifier") String identifier);


    List<Patient> findByVoidedFalse();

    @Query("""
                SELECT p FROM Patient p
                WHERE (:family IS NULL OR LOWER(p.familyName) LIKE LOWER(CONCAT('%', :family, '%')))
                  AND (:given IS NULL OR LOWER(p.givenName) LIKE LOWER(CONCAT('%', :given, '%')))
                  AND (:identifier IS NULL OR p.patientIdentifier = :identifier)
                  AND (:birthDate IS NULL OR p.dateOfBirth = :birthDate)
            """)
    List<Patient> filterPatientByParams(
            @Param("family") String family,
            @Param("given") String given,
            @Param("identifier") String identifier,
            @Param("birthDate") LocalDate birthDate
    );


}