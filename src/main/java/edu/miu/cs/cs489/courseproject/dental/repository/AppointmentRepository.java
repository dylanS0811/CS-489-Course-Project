package edu.miu.cs.cs489.courseproject.dental.repository;

import edu.miu.cs.cs489.courseproject.dental.domain.Appointment;
import edu.miu.cs.cs489.courseproject.dental.domain.AppointmentStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByOrderByAppointmentDateAscAppointmentTimeAsc();

    List<Appointment> findTop8ByStatusAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAscAppointmentTimeAsc(
            AppointmentStatus status,
            LocalDate appointmentDate
    );

    boolean existsByDentist_DentistIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Long dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus excludedStatus
    );

    long countByDentist_DentistIdAndAppointmentDateBetweenAndStatusIn(
            Long dentistId,
            LocalDate startDate,
            LocalDate endDate,
            Collection<AppointmentStatus> statuses
    );

    void deleteAllByPatient(Patient patient);
}
