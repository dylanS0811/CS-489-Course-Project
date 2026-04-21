package edu.miu.cs.cs489.courseproject.dental;

import edu.miu.cs.cs489.courseproject.dental.domain.Address;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientResponse;
import edu.miu.cs.cs489.courseproject.dental.service.MappingService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MappingServiceUnitTests {

    private final MappingService mappingService = new MappingService();

    @Test
    void shouldMapPatientEntityToPatientResponseDto() {
        Patient patient = new Patient(
                "P200",
                "Nina",
                "Patel",
                "641-451-3200",
                "nina.patel@example.com",
                LocalDate.of(1990, 5, 4),
                new Address("200 Wellness Way", "Fairfield", "IA", "52556")
        );

        PatientResponse response = mappingService.toPatientResponse(patient);

        assertThat(response.patientNumber()).isEqualTo("P200");
        assertThat(response.fullName()).isEqualTo("Nina Patel");
        assertThat(response.mailingAddress().city()).isEqualTo("Fairfield");
    }
}
