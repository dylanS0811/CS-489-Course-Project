package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.domain.AppointmentStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.dto.dashboard.DashboardResponse;
import edu.miu.cs.cs489.courseproject.dental.repository.AppointmentRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentalBillRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentistRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.PatientRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.SurgeryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;
    private final AppointmentRepository appointmentRepository;
    private final DentalBillRepository dentalBillRepository;
    private final MappingService mappingService;

    public DashboardService(PatientRepository patientRepository,
                            DentistRepository dentistRepository,
                            SurgeryRepository surgeryRepository,
                            AppointmentRepository appointmentRepository,
                            DentalBillRepository dentalBillRepository,
                            MappingService mappingService) {
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.surgeryRepository = surgeryRepository;
        this.appointmentRepository = appointmentRepository;
        this.dentalBillRepository = dentalBillRepository;
        this.mappingService = mappingService;
    }

    public DashboardResponse getDashboard() {
        return new DashboardResponse(
                patientRepository.count(),
                dentistRepository.count(),
                surgeryRepository.count(),
                appointmentRepository.count(),
                dentalBillRepository.countByStatus(BillStatus.UNPAID),
                appointmentRepository.findTop8ByStatusAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAscAppointmentTimeAsc(
                                AppointmentStatus.SCHEDULED,
                                LocalDate.now())
                        .stream()
                        .map(mappingService::toAppointmentResponse)
                        .toList()
        );
    }
}
