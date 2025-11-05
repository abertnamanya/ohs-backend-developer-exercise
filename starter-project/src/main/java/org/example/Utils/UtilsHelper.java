package org.example.Utils;

public class UtilsHelper {

    public enum PatientIdentifiersType {
        DRIVING_PERMIT,
        NATIONAL_ID,
        PASSPORT,
        WORK_ID,
        OTHER;

        public static PatientIdentifiersType fromString(String value) {
            if (value == null) return null;
            switch (value.toLowerCase()) {
                case "driving permit" -> {
                    return DRIVING_PERMIT;
                }
                case "national id" -> {
                    return NATIONAL_ID;
                }
                case "passport" -> {
                    return PASSPORT;
                }
                case "work id" -> {
                    return WORK_ID;
                }
                default -> {
                    return OTHER;
                }
            }
        }
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER;

        public static Gender fromString(String value) {
            if (value == null) return null;
            switch (value.toLowerCase()) {
                case "male" -> {
                    return MALE;
                }
                case "female" -> {
                    return FEMALE;
                }
                default -> {
                    return OTHER;
                }
            }
        }
    }

    public enum EncounterClass {
        REGISTRATION,
        VITALS_CHECK,
        OTHER;

        public static EncounterClass fromString(String value) {
            if (value == null) return null;
            switch (value.toLowerCase()) {
                case "registration" -> {
                    return REGISTRATION;
                }
                case "vitals check" -> {
                    return VITALS_CHECK;
                }
                default -> {
                    return OTHER;
                }
            }
        }
    }
}