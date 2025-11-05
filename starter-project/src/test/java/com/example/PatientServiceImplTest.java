package com.example;

import org.example.Patient;
import org.example.Utils.ResourceNotFoundException;
import org.example.Utils.UtilsHelper.PatientIdentifiersType;
import org.example.Utils.UtilsHelper.Gender;
import org.example.mapper.PatientMapper;
import org.example.repository.PatientRepository;
import org.example.requests.PatientRequest;
import org.example.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    private PatientRepository patientRepository;
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientRequest patientRequest;
    private LocalDate birthDate;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);

        patientService = new PatientServiceImpl(patientRepository, null);

        birthDate = LocalDate.of(1990, 1, 1);

        patient = new Patient();
        patient.setId(1L);
        patient.setGivenName("Julius");
        patient.setFamilyName("Igoma");
        patient.setGender(Gender.MALE);
        patient.setDateOfBirth(birthDate);
        patient.setPatientIdentifier("UG-2SGD8-12");
        patient.setPatientIdentifierType(PatientIdentifiersType.NATIONAL_ID);
        patient.setVoided(false);

        patientRequest = new PatientRequest();
        patientRequest.setGivenName("Julius");
        patientRequest.setFamilyName("Igoma");
        patientRequest.setGender("MALE");
        patientRequest.setBirthDate(birthDate);
        patientRequest.setPatientIdentifier("UG-2SGD8-12");
        patientRequest.setPatientIdentifierType("NATIONAL_ID");
    }

    @Test
    void findByPatientId_WithValidId_ShouldReturnPatientRequest() {
        try (MockedStatic<PatientMapper> patientMapperMock = mockStatic(PatientMapper.class)) {

            when(patientRepository.findPatientById(1L)).thenReturn(patient);
            patientMapperMock.when(() -> PatientMapper.toDto(patient)).thenReturn(patientRequest);

            PatientRequest result = patientService.findByPatientId(1L);

            assertNotNull(result);
            assertEquals(patientRequest, result);
            verify(patientRepository).findPatientById(1L);
            patientMapperMock.verify(() -> PatientMapper.toDto(patient));
        }
    }

    @Test
    void findByPatientId_WithInvalidId_ShouldThrowException() {

        when(patientRepository.findPatientById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.findByPatientId(1L));

        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).findPatientById(1L);
    }

    @Test
    void findByPatientIdReturnPatient_WithValidId_ShouldReturnPatient() {

        when(patientRepository.findPatientById(1L)).thenReturn(patient);

        Patient result = patientService.findByPatientIdReturnPatient(1L);

        assertNotNull(result);
        assertEquals(patient, result);
        verify(patientRepository).findPatientById(1L);
    }

    @Test
    void savePatient_WithValidRequest_ShouldReturnPatientRequest() {
        try (MockedStatic<PatientMapper> patientMapperMock = mockStatic(PatientMapper.class)) {

            when(patientRepository.findPatientByPatientIdentifier("UG-2SGD8-12")).thenReturn(null);
            patientMapperMock.when(() -> PatientMapper.toEntity(patientRequest)).thenReturn(patient);
            when(patientRepository.save(patient)).thenReturn(patient);
            patientMapperMock.when(() -> PatientMapper.toDto(patient)).thenReturn(patientRequest);

            PatientRequest result = patientService.savePatient(patientRequest);

            assertNotNull(result);
            assertEquals(patientRequest, result);
            verify(patientRepository).findPatientByPatientIdentifier("UG-2SGD8-12");
            patientMapperMock.verify(() -> PatientMapper.toEntity(patientRequest));
            verify(patientRepository).save(patient);
            patientMapperMock.verify(() -> PatientMapper.toDto(patient));
        }
    }

    @Test
    void savePatient_WithExistingIdentifier_ShouldThrowException() {

        when(patientRepository.findPatientByPatientIdentifier("UG-2SGD8-12")).thenReturn(patient);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.savePatient(patientRequest));

        assertEquals("Patient with identifier UG-2SGD8-12 already exists", exception.getMessage());
        verify(patientRepository).findPatientByPatientIdentifier("UG-2SGD8-12");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getAllActivePatients_WithActivePatients_ShouldReturnList() {
        try (MockedStatic<PatientMapper> patientMapperMock = mockStatic(PatientMapper.class)) {

            List<Patient> patients = Arrays.asList(patient);
            List<PatientRequest> patientRequests = Arrays.asList(patientRequest);

            when(patientRepository.findByVoidedFalse()).thenReturn(patients);
            patientMapperMock.when(() -> PatientMapper.toDto(patient)).thenReturn(patientRequest);

            List<PatientRequest> result = patientService.getAllActivePatients();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(patientRequests, result);
            verify(patientRepository).findByVoidedFalse();
            patientMapperMock.verify(() -> PatientMapper.toDto(patient));
        }
    }

    @Test
    void getAllActivePatients_WithNoActivePatients_ShouldThrowException() {

        when(patientRepository.findByVoidedFalse()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.getAllActivePatients());

        assertEquals("No active patients found", exception.getMessage());
        verify(patientRepository).findByVoidedFalse();
    }

    @Test
    void findPatientByIdentifier_WithValidIdentifier_ShouldReturnPatientRequest() {
        try (MockedStatic<PatientMapper> patientMapperMock = mockStatic(PatientMapper.class)) {

            when(patientRepository.findPatientByPatientIdentifier("UG-2SGD8-12")).thenReturn(patient);
            patientMapperMock.when(() -> PatientMapper.toDto(patient)).thenReturn(patientRequest);

            PatientRequest result = patientService.findPatientByIdentifier("UG-2SGD8-12");

            assertNotNull(result);
            assertEquals(patientRequest, result);
            verify(patientRepository).findPatientByPatientIdentifier("UG-2SGD8-12");
            patientMapperMock.verify(() -> PatientMapper.toDto(patient));
        }
    }

    @Test
    void findPatientByIdentifier_WithInvalidIdentifier_ShouldThrowException() {

        when(patientRepository.findPatientByPatientIdentifier("INVALID")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.findPatientByIdentifier("INVALID"));

        assertEquals("Patient with identifier INVALID not found", exception.getMessage());
        verify(patientRepository).findPatientByPatientIdentifier("INVALID");
    }

    @Test
    void deletePatient_WithValidPatientId_ShouldVoidPatient() {

        when(patientRepository.findPatientById(1L)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);

        patientService.deletePatient(1L);

        assertTrue(patient.isVoided());
        verify(patientRepository).findPatientById(1L);
        verify(patientRepository).save(patient);
    }

    @Test
    void deletePatient_WithInvalidPatientId_ShouldThrowException() {

        when(patientRepository.findPatientById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.deletePatient(1L));

        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).findPatientById(1L);
        verify(patientRepository, never()).save(any(Patient.class));
    }
}