package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.domain.Address;
import edu.miu.cs.cs489.courseproject.dental.domain.AppUser;
import edu.miu.cs.cs489.courseproject.dental.domain.Appointment;
import edu.miu.cs.cs489.courseproject.dental.domain.AppointmentStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.DentalBill;
import edu.miu.cs.cs489.courseproject.dental.domain.Dentist;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import edu.miu.cs.cs489.courseproject.dental.domain.Role;
import edu.miu.cs.cs489.courseproject.dental.domain.Surgery;
import edu.miu.cs.cs489.courseproject.dental.repository.AppUserRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.AppointmentRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentalBillRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.DentistRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.PatientRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.RoleRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.SurgeryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ClinicSeedService {

    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;
    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DentalBillRepository dentalBillRepository;
    private final PasswordEncoder passwordEncoder;

    public ClinicSeedService(RoleRepository roleRepository,
                             AppUserRepository appUserRepository,
                             DentistRepository dentistRepository,
                             SurgeryRepository surgeryRepository,
                             PatientRepository patientRepository,
                             AppointmentRepository appointmentRepository,
                             DentalBillRepository dentalBillRepository,
                             PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.appUserRepository = appUserRepository;
        this.dentistRepository = dentistRepository;
        this.surgeryRepository = surgeryRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.dentalBillRepository = dentalBillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seedDemoData() {
        if (hasAnyData()) {
            return;
        }
        seedFixture();
    }

    public void resetDemoData() {
        clearData();
        seedFixture();
    }

    private void seedFixture() {
        Role officeManagerRole = roleRepository.save(
                new Role("OFFICE_MANAGER", "Registers patients and schedules dental appointments"));
        Role administratorRole = roleRepository.save(
                new Role("ADMINISTRATOR", "Maintains master data, users, and clinical configuration"));

        AppUser officeManager = appUserRepository.save(new AppUser(
                "HainingSong",
                passwordEncoder.encode("welcome1"),
                "Haining",
                "Song",
                "haining.song@brightsmile.example",
                officeManagerRole
        ));
        appUserRepository.save(new AppUser(
                "ethan.reed",
                passwordEncoder.encode("welcome1"),
                "Ethan",
                "Reed",
                "ethan.reed@brightsmile.example",
                administratorRole
        ));

        Dentist tonySmith = dentistRepository.save(new Dentist(
                "D100", "Tony", "Smith", "641-451-1000",
                "tony.smith@brightsmile.example", "General Dentistry"));
        Dentist helenPearson = dentistRepository.save(new Dentist(
                "D105", "Helen", "Pearson", "641-451-1005",
                "helen.pearson@brightsmile.example", "Endodontics"));
        Dentist robinPlevin = dentistRepository.save(new Dentist(
                "D110", "Robin", "Plevin", "641-451-1010",
                "robin.plevin@brightsmile.example", "Oral Surgery"));
        dentistRepository.save(new Dentist(
                "D115", "Amelia", "Roberts", "641-451-1015",
                "amelia.roberts@brightsmile.example", "Pediatric Dentistry"));

        Surgery surgery10 = surgeryRepository.save(new Surgery(
                "S10", "BrightSmile North", "641-451-2010",
                new Address("10 North Hill Rd", "Fairfield", "IA", "52556")));
        Surgery surgery13 = surgeryRepository.save(new Surgery(
                "S13", "BrightSmile Riverside", "641-451-2013",
                new Address("13 Riverside Ave", "Fairfield", "IA", "52556")));
        Surgery surgery15 = surgeryRepository.save(new Surgery(
                "S15", "BrightSmile Downtown", "641-451-2015",
                new Address("15 Main St", "Fairfield", "IA", "52556")));

        Patient gillianWhite = patientRepository.save(new Patient(
                "P100", "Gillian", "White", "641-451-3000", "gillian.white@example.com",
                LocalDate.of(1989, 4, 12), new Address("100 Maple St", "Fairfield", "IA", "52556")));
        Patient jillBell = patientRepository.save(new Patient(
                "P105", "Jill", "Bell", "641-451-3005", "jill.bell@example.com",
                LocalDate.of(1991, 7, 21), new Address("105 Cedar Ave", "Fairfield", "IA", "52556")));
        Patient ianMacKay = patientRepository.save(new Patient(
                "P108", "Ian", "MacKay", "641-451-3008", "ian.mackay@example.com",
                LocalDate.of(1987, 11, 3), new Address("108 Birch Ln", "Fairfield", "IA", "52556")));
        Patient johnWalker = patientRepository.save(new Patient(
                "P110", "John", "Walker", "641-451-3010", "john.walker@example.com",
                LocalDate.of(1994, 2, 9), new Address("110 Walnut Dr", "Fairfield", "IA", "52556")));
        Patient mariaLopez = patientRepository.save(new Patient(
                "P115", "Maria", "Lopez", "641-451-3015", "maria.lopez@example.com",
                LocalDate.of(1994, 6, 15), new Address("115 Oak Ct", "Fairfield", "IA", "52556")));

        List<Appointment> appointments = appointmentRepository.saveAll(List.of(
                new Appointment(LocalDate.now().plusDays(1), LocalTime.of(9, 0), AppointmentStatus.SCHEDULED,
                        true, "Routine dental consultation", gillianWhite, tonySmith, surgery15, officeManager),
                new Appointment(LocalDate.now().plusDays(1), LocalTime.of(11, 30), AppointmentStatus.SCHEDULED,
                        true, "Wisdom tooth review", jillBell, robinPlevin, surgery15, officeManager),
                new Appointment(LocalDate.now().plusDays(2), LocalTime.of(10, 0), AppointmentStatus.SCHEDULED,
                        true, "Root canal follow-up", ianMacKay, helenPearson, surgery10, officeManager),
                new Appointment(LocalDate.now().plusDays(3), LocalTime.of(14, 0), AppointmentStatus.SCHEDULED,
                        true, "Orthodontic consultation", johnWalker, helenPearson, surgery10, officeManager),
                new Appointment(LocalDate.now().minusDays(4), LocalTime.of(16, 30), AppointmentStatus.COMPLETED,
                        true, "Surgical extraction", jillBell, robinPlevin, surgery13, officeManager)
        ));

        dentalBillRepository.saveAll(List.of(
                new DentalBill(gillianWhite, appointments.get(0), LocalDate.now().minusDays(3),
                        new BigDecimal("120.00"), BillStatus.PAID, "Routine dental consultation"),
                new DentalBill(ianMacKay, appointments.get(2), LocalDate.now().minusDays(2),
                        new BigDecimal("300.00"), BillStatus.UNPAID, "Root canal treatment follow-up"),
                new DentalBill(mariaLopez, null, LocalDate.now().minusDays(8),
                new BigDecimal("180.00"), BillStatus.UNPAID, "Outstanding balance from previous procedure")
        ));
    }

    private boolean hasAnyData() {
        return roleRepository.count() > 0
                || appUserRepository.count() > 0
                || dentistRepository.count() > 0
                || surgeryRepository.count() > 0
                || patientRepository.count() > 0
                || appointmentRepository.count() > 0
                || dentalBillRepository.count() > 0;
    }

    private void clearData() {
        dentalBillRepository.deleteAllInBatch();
        appointmentRepository.deleteAllInBatch();
        appUserRepository.deleteAllInBatch();
        patientRepository.deleteAllInBatch();
        surgeryRepository.deleteAllInBatch();
        dentistRepository.deleteAllInBatch();
        roleRepository.deleteAllInBatch();
    }
}
