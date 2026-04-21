package edu.miu.cs.cs489.courseproject.dental.repository;

import edu.miu.cs.cs489.courseproject.dental.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientNumber(String patientNumber);

    List<Patient> findAllByOrderByLastNameAscFirstNameAsc();

    @Query("""
            select distinct p
            from Patient p
            left join p.mailingAddress a
            where lower(p.patientNumber) like lower(concat('%', :term, '%'))
               or lower(p.firstName) like lower(concat('%', :term, '%'))
               or lower(p.lastName) like lower(concat('%', :term, '%'))
               or lower(p.phoneNumber) like lower(concat('%', :term, '%'))
               or lower(p.email) like lower(concat('%', :term, '%'))
               or lower(a.city) like lower(concat('%', :term, '%'))
               or lower(a.state) like lower(concat('%', :term, '%'))
               or lower(a.zipCode) like lower(concat('%', :term, '%'))
            order by p.lastName asc, p.firstName asc
            """)
    List<Patient> search(@Param("term") String term);
}
