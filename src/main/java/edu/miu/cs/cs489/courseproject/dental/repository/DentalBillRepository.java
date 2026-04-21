package edu.miu.cs.cs489.courseproject.dental.repository;

import edu.miu.cs.cs489.courseproject.dental.domain.BillStatus;
import edu.miu.cs.cs489.courseproject.dental.domain.DentalBill;
import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentalBillRepository extends JpaRepository<DentalBill, Long> {

    boolean existsByPatient_PatientIdAndStatus(Long patientId, BillStatus status);

    long countByStatus(BillStatus status);

    List<DentalBill> findAllByOrderByIssueDateDescBillIdDesc();

    List<DentalBill> findByStatusOrderByIssueDateDescBillIdDesc(BillStatus status);

    void deleteAllByPatient(Patient patient);
}
