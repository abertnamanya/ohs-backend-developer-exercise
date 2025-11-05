package com.example;

import org.example.Encounter;
import org.example.Patient;
import org.example.Utils.ResourceNotFoundException;
import org.example.mapper.EncounterMapper;
import org.example.repository.EncounterRepository;
import org.example.repository.PatientRepository;
import org.example.requests.EncounterRequest;
import org.example.service.EncounterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EncounterServiceImplTest {

    private EncounterRepository encounterRepository;
    private PatientRepository patientRepository;
    private EncounterServiceImpl encounterService;

    private Patient patient;
    private Encounter encounter;
    private EncounterRequest encounterRequest;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        encounterRepository = mock(EncounterRepository.class);
        patientRepository = mock(PatientRepository.class);
        encounterService = new EncounterServiceImpl(encounterRepository, patientRepository);

        now = LocalDateTime.now();

        patient = new Patient();
        patient.setId(1L);
        patient.setFamilyName("Julius");
        patient.setGivenName("Igoma");

        encounter = new Encounter();
        encounter.setId(1L);
        encounter.setPatient(patient);
        encounter.setStart(now);
        encounter.setEnd(now.plusHours(1));
        encounter.setVoided(false);

        encounterRequest = new EncounterRequest();
        encounterRequest.setPatientId(1L);
        encounterRequest.setStart(now);
        encounterRequest.setEnd(now.plusHours(1));
    }

    @Test
    void createEncounter_WithValidRequest_ShouldReturnEncounterRequest() {
        try (MockedStatic<EncounterMapper> encounterMapperMock = mockStatic(EncounterMapper.class)) {
            when(patientRepository.findPatientById(1L)).thenReturn(patient);
            encounterMapperMock.when(() -> EncounterMapper.toEntity(encounterRequest, patient)).thenReturn(encounter);
            when(encounterRepository.save(encounter)).thenReturn(encounter);
            encounterMapperMock.when(() -> EncounterMapper.toDto(encounter)).thenReturn(encounterRequest);

            EncounterRequest result = encounterService.createEncounter(encounterRequest);

            assertNotNull(result);
            assertEquals(encounterRequest, result);
            verify(patientRepository).findPatientById(1L);
            encounterMapperMock.verify(() -> EncounterMapper.toEntity(encounterRequest, patient));
            verify(encounterRepository).save(encounter);
            encounterMapperMock.verify(() -> EncounterMapper.toDto(encounter));
        }
    }

    @Test
    void createEncounter_WithInvalidPatientId_ShouldThrowException() {
        when(patientRepository.findPatientById(1L)).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> encounterService.createEncounter(encounterRequest));

        assertEquals("Invalid patient id 1", exception.getMessage());
        verify(patientRepository).findPatientById(1L);
        verify(encounterRepository, never()).save(any(Encounter.class));
    }

    @Test
    void findEncounterById_WithValidId_ShouldReturnEncounter() {
        when(encounterRepository.findEncounterById(1L)).thenReturn(encounter);
        Encounter result = encounterService.findEncounterById(1L);
        assertNotNull(result);
        assertEquals(encounter, result);
        verify(encounterRepository).findEncounterById(1L);
    }

    @Test
    void findEncounterById_WithInvalidId_ShouldThrowException() {

        when(encounterRepository.findEncounterById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> encounterService.findEncounterById(1L));

        assertEquals("Invalid encounter id 1", exception.getMessage());
        verify(encounterRepository).findEncounterById(1L);
    }

    @Test
    void findEncounterByIdReturnEncounterRequest_WithValidId_ShouldReturnEncounterRequest() {
        try (MockedStatic<EncounterMapper> encounterMapperMock = mockStatic(EncounterMapper.class)) {
            when(encounterRepository.findEncounterById(1L)).thenReturn(encounter);
            encounterMapperMock.when(() -> EncounterMapper.toDto(encounter)).thenReturn(encounterRequest);

            EncounterRequest result = encounterService.findEncounterByIdReturnEncounterRequest(1L);

            assertNotNull(result);
            assertEquals(encounterRequest, result);
            verify(encounterRepository).findEncounterById(1L);
            encounterMapperMock.verify(() -> EncounterMapper.toDto(encounter));
        }
    }

    @Test
    void updateEncounter_WithValidRequest_ShouldReturnUpdatedEncounterRequest() {
        try (MockedStatic<EncounterMapper> encounterMapperMock = mockStatic(EncounterMapper.class)) {
            EncounterRequest updatedRequest = new EncounterRequest();
            updatedRequest.setPatientId(2L);
            updatedRequest.setStart(now.plusDays(1));

            Patient updatedPatient = new Patient();
            updatedPatient.setId(2L);
            updatedPatient.setFamilyName("Muranga");
            updatedPatient.setGivenName("Igoma");

            when(encounterRepository.findEncounterById(1L)).thenReturn(encounter);
            when(patientRepository.findPatientById(2L)).thenReturn(updatedPatient);
            when(encounterRepository.save(encounter)).thenReturn(encounter);
            encounterMapperMock.when(() -> EncounterMapper.toDto(encounter)).thenReturn(updatedRequest);

            EncounterRequest result = encounterService.updateEncounter(updatedRequest, 1L);

            assertNotNull(result);
            assertEquals(updatedRequest, result);
            verify(encounterRepository).findEncounterById(1L);
            verify(patientRepository).findPatientById(2L);
            verify(encounterRepository).save(encounter);
            encounterMapperMock.verify(() -> EncounterMapper.toDto(encounter));
        }
    }

    @Test
    void updateEncounter_WithNonExistentEncounter_ShouldThrowException() {
        when(encounterRepository.findEncounterById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> encounterService.updateEncounter(encounterRequest, 1L));

        assertEquals("Encounter not found with id: 1", exception.getMessage());
        verify(encounterRepository).findEncounterById(1L);
        verify(patientRepository, never()).findPatientById(anyLong());
        verify(encounterRepository, never()).save(any(Encounter.class));
    }

    @Test
    void deletePatient_WithValidEncounterId_ShouldVoidEncounter() {
        when(encounterRepository.findEncounterById(1L)).thenReturn(encounter);
        when(encounterRepository.save(encounter)).thenReturn(encounter);

        encounterService.deletePatient(1L);

        assertTrue(encounter.isVoided());
        verify(encounterRepository).findEncounterById(1L);
        verify(encounterRepository).save(encounter);
    }

    @Test
    void deletePatient_WithInvalidEncounterId_ShouldThrowException() {
        when(encounterRepository.findEncounterById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> encounterService.deletePatient(1L));

        assertEquals("Encounter not found with id: 1", exception.getMessage());
        verify(encounterRepository).findEncounterById(1L);
        verify(encounterRepository, never()).save(any(Encounter.class));
    }

    @Test
    void getAllNonVoidedEncounters_WithEncounters_ShouldReturnList() {
        try (MockedStatic<EncounterMapper> encounterMapperMock = mockStatic(EncounterMapper.class)) {
            List<Encounter> encounters = Arrays.asList(encounter);
            List<EncounterRequest> encounterRequests = Arrays.asList(encounterRequest);

            when(encounterRepository.findByVoidedFalse()).thenReturn(encounters);
            encounterMapperMock.when(() -> EncounterMapper.toDto(encounter)).thenReturn(encounterRequest);

            List<EncounterRequest> result = encounterService.getAllNonVoidedEncounters();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(encounterRequests, result);
            verify(encounterRepository).findByVoidedFalse();
            encounterMapperMock.verify(() -> EncounterMapper.toDto(encounter));
        }
    }

    @Test
    void getAllNonVoidedEncounters_WithNoEncounters_ShouldThrowException() {
        when(encounterRepository.findByVoidedFalse()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> encounterService.getAllNonVoidedEncounters());

        assertEquals("No encounters created yet", exception.getMessage());
        verify(encounterRepository).findByVoidedFalse();
    }

    @Test
    void getEncountersByPatientId_WithEncounters_ShouldReturnList() {
        try (MockedStatic<EncounterMapper> encounterMapperMock = mockStatic(EncounterMapper.class)) {
            List<Encounter> encounters = Arrays.asList(encounter);
            List<EncounterRequest> encounterRequests = Arrays.asList(encounterRequest);

            when(encounterRepository.findByPatientId(1L)).thenReturn(encounters);
            encounterMapperMock.when(() -> EncounterMapper.toDto(encounter)).thenReturn(encounterRequest);

            List<EncounterRequest> result = encounterService.getEncountersByPatientId(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(encounterRequests, result);
            verify(encounterRepository).findByPatientId(1L);
            encounterMapperMock.verify(() -> EncounterMapper.toDto(encounter));
        }
    }
}