package com.laispsicologia.PsychologySchedule.services;

import com.laispsicologia.PsychologySchedule.dto.ProfessionalDTO;
import com.laispsicologia.PsychologySchedule.entities.Professional;
import com.laispsicologia.PsychologySchedule.entities.enums.Relationship;
import com.laispsicologia.PsychologySchedule.repositories.ClientRepository;
import com.laispsicologia.PsychologySchedule.repositories.ProfessionalRepository;
import com.laispsicologia.PsychologySchedule.repositories.ProfessionalRepository;
import com.laispsicologia.PsychologySchedule.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfessionalService {

    @Autowired
    private ProfessionalRepository repository;

    public Page<ProfessionalDTO> findAll(Pageable pageable) {
        return repository.findAllActive(pageable).map(ProfessionalDTO::new);
    }

    public ProfessionalDTO findById(Long id) {
        Professional entity = getEntityById(id);
        return new ProfessionalDTO(entity);
    }

    public ProfessionalDTO insert(ProfessionalDTO dto) {
        Professional entity = new Professional();
        copyDtoToEntity(dto, entity);
        return new ProfessionalDTO(repository.save(entity));
    }

    public ProfessionalDTO update(Long id, ProfessionalDTO dto) {
        Professional entity = getEntityById(id);
        copyDtoToEntity(dto, entity);
        return new ProfessionalDTO(repository.save(entity));
    }

    public void delete(Long id) {
        Professional entity = getEntityById(id);
        entity.softDelete();
        repository.save(entity);
    }

    private Professional getEntityById(Long id) {
        return repository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found"));
    }

    private void copyDtoToEntity(ProfessionalDTO dto, Professional entity) {
        entity.setName(dto.getName());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
    }

}
