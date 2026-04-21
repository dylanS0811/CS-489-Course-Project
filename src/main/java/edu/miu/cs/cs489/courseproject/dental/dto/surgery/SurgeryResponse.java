package edu.miu.cs.cs489.courseproject.dental.dto.surgery;

import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressResponse;

public record SurgeryResponse(
        Long surgeryId,
        String surgeryNumber,
        String name,
        String phoneNumber,
        AddressResponse locationAddress
) {
}
