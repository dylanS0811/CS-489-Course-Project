package edu.miu.cs.cs489.courseproject.dental;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs489.courseproject.dental.domain.Dentist;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.domain.Surgery;
import edu.miu.cs.cs489.courseproject.dental.dto.address.AddressRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.appointment.AppointmentRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.auth.LoginRequest;
import edu.miu.cs.cs489.courseproject.dental.dto.patient.PatientRequest;
import edu.miu.cs.cs489.courseproject.dental.repository.DentistRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.PatientRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.SurgeryRepository;
import edu.miu.cs.cs489.courseproject.dental.service.ClinicSeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DentalSurgeryApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClinicSeedService clinicSeedService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private SurgeryRepository surgeryRepository;

    @BeforeEach
    void setUp() {
        clinicSeedService.seedDemoData();
    }

    @Test
    void shouldAuthenticateAndProtectApiEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("Authentication is required")));

        String token = login("HainingSong", "welcome1");

        mockMvc.perform(get("/api/v1/dashboard").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientCount").value(5))
                .andExpect(jsonPath("$.dentistCount").value(4))
                .andExpect(jsonPath("$.surgeryCount").value(3))
                .andExpect(jsonPath("$.unpaidBillCount").value(2))
                .andExpect(jsonPath("$.upcomingAppointments", hasSize(greaterThanOrEqualTo(4))));
    }

    @Test
    void shouldRegisterPatientAndScheduleAppointment() throws Exception {
        String token = login("HainingSong", "welcome1");
        PatientRequest patientRequest = new PatientRequest(
                "P130",
                "Maria",
                "Anderson",
                "641-451-3130",
                "maria.anderson@example.com",
                LocalDate.of(1992, 8, 14),
                new AddressRequest("130 Elm St", "Fairfield", "IA", "52556")
        );

        MvcResult createPatientResult = mockMvc.perform(post("/api/v1/patients")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientNumber").value("P130"))
                .andExpect(jsonPath("$.mailingAddress.city").value("Fairfield"))
                .andReturn();

        Long patientId = objectMapper.readTree(createPatientResult.getResponse().getContentAsString())
                .get("patientId")
                .asLong();
        Dentist dentist = dentistRepository.findByDentistCode("D115").orElseThrow();
        Surgery surgery = surgeryRepository.findBySurgeryNumber("S13").orElseThrow();

        AppointmentRequest appointmentRequest = new AppointmentRequest(
                patientId,
                dentist.getDentistId(),
                surgery.getSurgeryId(),
                LocalDate.now().plusDays(6),
                LocalTime.of(9, 45),
                "New-patient consultation"
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patient.patientNumber").value("P130"))
                .andExpect(jsonPath("$.dentist.dentistCode").value("D115"))
                .andExpect(jsonPath("$.surgery.surgeryNumber").value("S13"))
                .andExpect(jsonPath("$.confirmationSent").value(true));
    }

    @Test
    void shouldRejectSchedulingWhenPatientHasUnpaidBill() throws Exception {
        String token = login("HainingSong", "welcome1");
        Patient unpaidPatient = patientRepository.findByPatientNumber("P108").orElseThrow();
        Dentist dentist = dentistRepository.findByDentistCode("D100").orElseThrow();
        Surgery surgery = surgeryRepository.findBySurgeryNumber("S10").orElseThrow();

        AppointmentRequest request = new AppointmentRequest(
                unpaidPatient.getPatientId(),
                dentist.getDentistId(),
                surgery.getSurgeryId(),
                LocalDate.now().plusDays(7),
                LocalTime.of(15, 0),
                "Follow-up request"
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("outstanding unpaid")));
    }

    @Test
    void shouldEnforceDentistWeeklyAppointmentLimit() throws Exception {
        String token = login("HainingSong", "welcome1");
        Patient patient = patientRepository.findByPatientNumber("P100").orElseThrow();
        Dentist dentist = dentistRepository.findByDentistCode("D115").orElseThrow();
        Surgery surgery = surgeryRepository.findBySurgeryNumber("S15").orElseThrow();
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        for (int i = 0; i < 5; i++) {
            AppointmentRequest request = new AppointmentRequest(
                    patient.getPatientId(),
                    dentist.getDentistId(),
                    surgery.getSurgeryId(),
                    nextMonday,
                    LocalTime.of(9 + i, 0),
                    "Weekly load test"
            );
            mockMvc.perform(post("/api/v1/appointments")
                            .header(HttpHeaders.AUTHORIZATION, bearer(token))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        AppointmentRequest sixthRequest = new AppointmentRequest(
                patient.getPatientId(),
                dentist.getDentistId(),
                surgery.getSurgeryId(),
                nextMonday.plusDays(1),
                LocalTime.of(15, 0),
                "Limit should reject this request"
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sixthRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("five active appointments")));
    }

    private String login(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("accessToken").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
