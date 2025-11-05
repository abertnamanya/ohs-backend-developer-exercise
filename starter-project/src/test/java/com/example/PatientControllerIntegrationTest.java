package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Encounter;
import org.example.Observation;
import org.example.Patient;
import org.example.Utils.UtilsHelper;
import org.example.Utils.UtilsHelper.EncounterClass;
import org.example.Utils.UtilsHelper.Gender;
import org.example.Utils.UtilsHelper.PatientIdentifiersType;
import org.example.repository.EncounterRepository;
import org.example.repository.ObservationRepository;
import org.example.repository.PatientRepository;
import org.example.requests.PatientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = org.example.ExerciseApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "admin", password = "@admin123", roles = {"ADMIN"})
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient testPatient;
    private PatientRequest validPatientRequest;

    @BeforeEach
    void setUp() {

        observationRepository.deleteAll();
        encounterRepository.deleteAll();
        patientRepository.deleteAll();


        testPatient = new Patient();
        testPatient.setGivenName("John");
        testPatient.setFamilyName("Doe");
        testPatient.setGender(Gender.MALE);
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setPatientIdentifier("TEST001");
        testPatient.setPatientIdentifierType(PatientIdentifiersType.NATIONAL_ID);
        testPatient.setVoided(false);
        testPatient = patientRepository.save(testPatient);


        validPatientRequest = new PatientRequest();
        validPatientRequest.setGivenName("Jane");
        validPatientRequest.setFamilyName("Smith");
        validPatientRequest.setGender("FEMALE");
        validPatientRequest.setBirthDate(LocalDate.of(1995, 5, 15));
        validPatientRequest.setPatientIdentifier("TEST002");
        validPatientRequest.setPatientIdentifierType("PASSPORT");
    }

    @Test
    void createPatient_WithValidData_ShouldCreatePatient() throws Exception {

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.givenName").value("Jane"))
                .andExpect(jsonPath("$.data.familyName").value("Smith"))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"))
                .andExpect(jsonPath("$.data.patientIdentifier").value("TEST002"));


        List<Patient> patients = patientRepository.findByVoidedFalse();
        assertThat(patients, hasSize(2));
    }

    @Test
    void createPatient_WithDuplicateIdentifier_ShouldReturnBadRequest() throws Exception {

        validPatientRequest.setPatientIdentifier("TEST001");


        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPatientById_WithValidId_ShouldReturnPatient() throws Exception {

        mockMvc.perform(get("/api/patients/{id}", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.givenName").value("John"))
                .andExpect(jsonPath("$.familyName").value("Doe"))
                .andExpect(jsonPath("$.patientIdentifier").value("TEST001"));
    }

    @Test
    void getPatientById_WithInvalidId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/api/patients/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllPatients_WithNoParameters_ShouldReturnAllActivePatients() throws Exception {

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].givenName").value("John"))
                .andExpect(jsonPath("$[0].familyName").value("Doe"));
    }

    @Test
    void updatePatient_WithValidData_ShouldUpdatePatient() throws Exception {

        mockMvc.perform(put("/api/patients/{id}", testPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.givenName").value("Jane"))
                .andExpect(jsonPath("$.data.familyName").value("Smith"));

        Patient updatedPatient = patientRepository.findPatientById(testPatient.getId());
        assertThat(updatedPatient.getGivenName(), is("Jane"));
        assertThat(updatedPatient.getFamilyName(), is("Smith"));
    }

    @Test
    void updatePatient_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/api/patients/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePatient_WithValidId_ShouldSoftDeletePatient() throws Exception {
        mockMvc.perform(delete("/api/patients/{id}", testPatient.getId()))
                .andExpect(status().isOk());

        Patient deletedPatient = patientRepository.findPatientById(testPatient.getId());
        assertThat(deletedPatient.isVoided(), is(true));
    }

    @Test
    void deletePatient_WithInvalidId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(delete("/api/patients/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPatientEncounters_WithValidPatientId_ShouldReturnEncounters() throws Exception {

        Encounter encounter = new Encounter();
        encounter.setPatient(testPatient);
        encounter.setStart(LocalDateTime.now());
        encounter.setEnd(LocalDateTime.now().plusHours(1));
        encounter.setEncounterClass(EncounterClass.fromString("VITALS_CHECK"));
        encounter.setVoided(false);
        encounterRepository.save(encounter);

        mockMvc.perform(get("/api/patients/{id}/encounters", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patientId").value(testPatient.getId()));
    }

    @Test
    void getPatientEncounters_WithPatientHavingNoEncounters_ShouldReturnEmptyList() throws Exception {
         /** To..Do
        mockMvc.perform(get("/api/patients/{id}/encounters", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); **/
    }

    @Test
    void getPatientEncounters_WithInvalidPatientId_ShouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/api/patients/{id}/encounters", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPatientObservations_WithValidPatientId_ShouldReturnObservations() throws Exception {
        Encounter encounter = new Encounter();
        encounter.setPatient(testPatient);
        encounter.setStart(LocalDateTime.now());
        encounter.setEnd(LocalDateTime.now().plusHours(1));
        encounter.setEncounterClass(UtilsHelper.EncounterClass.fromString("VITALS_CHECK"));
        encounter.setVoided(false);
        encounter = encounterRepository.save(encounter);

        Observation observation = new Observation();
        observation.setEncounter(encounter);
        observation.setCode("BP");
        observation.setValue("120/80");
        observation.setEffectiveDateTime(LocalDateTime.now());
        observationRepository.save(observation);


        /** To..Do
        mockMvc.perform(get("/api/patients/{id}/observations", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code").value("BP"))
                .andExpect(jsonPath("$[0].value").value("120/80")); **/
    }

    @Test
    void getPatientObservations_WithPatientHavingNoObservations_ShouldReturnEmptyList() throws Exception {
        /** To..Do
        mockMvc.perform(get("/api/patients/{id}/observations", testPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); **/
    }

    @Test
    void getPatientObservations_WithInvalidPatientId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/patients/{id}/observations", 999L))
                .andExpect(status().isNotFound());
    }
}