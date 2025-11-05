## REST API Documentation

##### Base Path ``http://localhost:8080``
### Authentication

All endpoints are protected by HTTP Basic Authentication.

Use these Credentials to login via PostMan or via endpoints request header ``
Username: admin
Password: @admin123

``

### API Endpoints

| Controller / Endpoint           | HTTP Method                           | Auth Required | Request Body                                                        | Response                                                                           |
| ------------------------------- | ------------------------------------- | ------------- | ------------------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| **Create Patient**              | POST `/api/patients`                  | Basic Auth    | `PatientRequest` JSON                                               | `{"message": "Patient created successfully", "data": PatientRequest}`              |
| **Get Patient by ID**           | GET `/api/patients/{id}`              | Basic Auth    | None                                                                | `PatientRequest`                                                                   |
| **Get All Patients**            | GET `/api/patients`                   | Basic Auth    | Optional query params: `family`, `given`, `identifier`, `birthDate` | List of `PatientRequest`                                                           |
| **Update Patient**              | PUT `/api/patients/{id}`              | Basic Auth    | `PatientRequest` JSON                                               | `{"message": "Patient updated successfully", "data": PatientRequest}`              |
| **Delete Patient**              | DELETE `/api/patients/{id}`           | Basic Auth    | None                                                                | `{"message": "Patient has been deleted successfully"}`                             |
| **Get Patient Encounters**      | GET `/api/patients/{id}/encounters`   | Basic Auth    | None                                                                | List of `EncounterRequest`                                                         |
| **Get Patient Observations**    | GET `/api/patients/{id}/observations` | Basic Auth    | None                                                                | `{"data": List<ObsRequest>}`                                                       |
| **Create Encounter**            | POST `/api/encounters`                | Basic Auth    | `EncounterRequest` JSON                                             | `{"message": "Encounter created successfully", "data": EncounterRequest}`            |
| **Get Encounter by ID**         | GET `/api/encounters/{id}`            | Basic Auth    | None                                                                | `EncounterRequest`                                                                 |
| **Get All Encounters / Filter** | GET `/api/encounters`                 | Basic Auth    | Optional query params: `start`, `end`, `encounterClass`             | List of `EncounterRequest`                                                         |
| **Update Encounter**            | PUT `/api/encounters/{id}`            | Basic Auth    | `EncounterRequest` JSON                                             | `{"message": "Encounter has been updated successfully", "data": EncounterRequest}` |
| **Delete Encounter**            | DELETE `/api/encounters/{id}`         | Basic Auth    | None                                                                | `{"message": "Encounter has been deleted successfully"}`                           |


### Patient Request Body

``
 {
    "patientIdentifier": "UG-87393-10",
    "patientIdentifierType": "WORK_ID",
    "givenName": "Samson",
    "familyName": "kakoma",
    "birthDate": "2014-04-06",
    "gender": "male"
}
``

### Encounter Request Body 
``
{
  "patientId": 1,
  "start": "2025-11-04T00:00:00",
  "end": "2025-11-04T00:00:00",
  "encounterClass": "VITALS_CHECK",
  "obs": [
    {"code": "CONCEPT_UUID_BP", "value": "120","effectiveDateTime": "2025-11-04T00:00:00"},
    {"code": "CONCEPT_UUID_BP_DIASTOLIC", "value": "80","effectiveDateTime": "2025-11-04T00:00:00"}
  ]
}
``