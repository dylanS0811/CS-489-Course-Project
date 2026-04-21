package edu.miu.cs.cs489.courseproject.dental.dto.address;

public record AddressResponse(
        Long addressId,
        String street,
        String city,
        String state,
        String zipCode
) {
}
