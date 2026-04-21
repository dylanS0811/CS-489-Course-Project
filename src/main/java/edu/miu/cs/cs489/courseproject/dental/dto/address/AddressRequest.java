package edu.miu.cs.cs489.courseproject.dental.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank @Size(max = 255) String street,
        @NotBlank @Size(max = 128) String city,
        @NotBlank @Size(max = 32) String state,
        @NotBlank @Size(max = 16) String zipCode
) {
}
