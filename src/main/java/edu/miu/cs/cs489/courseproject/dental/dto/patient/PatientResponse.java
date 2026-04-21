package edu.miu.cs.cs489.courseproject.dental.dto.patient;

import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressResponse;

import java.time.LocalDate;

public record PatientResponse(
        Long patientId,
        String patientNumber,
        String firstName,
        String lastName,
        String fullName,
        String phoneNumber,
        String email,
        LocalDate dateOfBirth,
        AddressResponse mailingAddress
) {
}
