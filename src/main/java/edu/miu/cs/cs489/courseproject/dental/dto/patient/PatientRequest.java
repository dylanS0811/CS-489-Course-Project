package edu.miu.cs.cs489.courseproject.dental.dto.patient;

import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank @Size(max = 16) String patientNumber,
        @NotBlank @Size(max = 64) String firstName,
        @NotBlank @Size(max = 64) String lastName,
        @NotBlank @Size(max = 32) String phoneNumber,
        @NotBlank @Email String email,
        @NotNull @Past LocalDate dateOfBirth,
        @NotNull @Valid AddressRequest mailingAddress
) {
}
