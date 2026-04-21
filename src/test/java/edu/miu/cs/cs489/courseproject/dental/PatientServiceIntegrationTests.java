package edu.miu.cs.cs489.courseproject.dental;

import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientResponse;
import edu.miu.cs.cs489.courseproject.dental.service.ClinicSeedService;
import edu.miu.cs.cs489.courseproject.dental.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PatientServiceIntegrationTests {

    @Autowired
    private ClinicSeedService clinicSeedService;

    @Autowired
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        clinicSeedService.seedDemoData();
    }

    @Test
    void shouldCreateAndSearchPatientThroughDtoLayer() {
        PatientResponse createdPatient = patientService.createPatient(new PatientRequest(
                "P140",
                "Ava",
                "Zimmer",
                "641-451-3140",
                "ava.zimmer@example.com",
                LocalDate.of(1995, 3, 20),
                new AddressRequest("140 Pine St", "Ottumwa", "IA", "52501")
        ));

        assertThat(createdPatient.patientId()).isNotNull();
        assertThat(createdPatient.fullName()).isEqualTo("Ava Zimmer");

        List<PatientResponse> searchResults = patientService.searchPatients("Ottumwa");

        assertThat(searchResults)
                .extracting(PatientResponse::patientNumber)
                .containsExactly("P140");
    }
}
