package edu.miu.cs.cs489.courseproject.dental.dto.patient;

public record PatientSummaryResponse(
        Long patientId,
        String patientNumber,
        String fullName,
        String phoneNumber,
        String email
) {
}
