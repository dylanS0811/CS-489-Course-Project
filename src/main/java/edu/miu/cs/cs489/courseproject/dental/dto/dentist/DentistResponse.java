package edu.miu.cs.cs489.courseproject.dental.dto.dentist;

public record DentistResponse(
        Long dentistId,
        String dentistCode,
        String firstName,
        String lastName,
        String fullName,
        String phoneNumber,
        String email,
        String specialization
) {
}
