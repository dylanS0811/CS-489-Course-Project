package edu.miu.cs.cs489.courseproject.dental.service;

import edu.miu.cs.cs489.courseproject.dental.dto.dentist.DentistResponse;
import edu.miu.cs.cs489.courseproject.dental.dto.surgery.SurgeryResponse;
import edu.miu.cs.cs489.courseproject.dental.repository.DentistRepository;
import edu.miu.cs.cs489.courseproject.dental.repository.SurgeryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DirectoryService {

    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;
    private final MappingService mappingService;

    public DirectoryService(DentistRepository dentistRepository,
                            SurgeryRepository surgeryRepository,
                            MappingService mappingService) {
        this.dentistRepository = dentistRepository;
        this.surgeryRepository = surgeryRepository;
        this.mappingService = mappingService;
    }

    public List<DentistResponse> getDentists() {
        return dentistRepository.findAllByOrderByLastNameAscFirstNameAsc()
                .stream()
                .map(mappingService::toDentistResponse)
                .toList();
    }

    public List<SurgeryResponse> getSurgeries() {
        return surgeryRepository.findAllByOrderBySurgeryNumberAsc()
                .stream()
                .map(mappingService::toSurgeryResponse)
                .toList();
    }
}
