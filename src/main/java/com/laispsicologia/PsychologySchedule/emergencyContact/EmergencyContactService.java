package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.Relationship;
import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmergencyContactService {

    @Autowired
    private EmergencyContactRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    public Page<EmergencyContactDTO> findAll(Pageable pageable) {
        return repository.findAllActive(pageable).map(EmergencyContactDTO::new);
    }

    public EmergencyContactDTO findById(Long id) {
        EmergencyContact entity = getEntityById(id);
        return new EmergencyContactDTO(entity);
    }

    public EmergencyContactDTO insert(EmergencyContactDTO dto) {
        EmergencyContact entity = new EmergencyContact();
        copyDtoToEntity(dto, entity);
        return new EmergencyContactDTO(repository.save(entity));
    }

    public EmergencyContactDTO update(Long id, EmergencyContactDTO dto) {
        EmergencyContact entity = getEntityById(id);
        copyDtoToEntity(dto, entity);
        return new EmergencyContactDTO(repository.save(entity));
    }

    public void delete(Long id) {
        EmergencyContact entity = getEntityById(id);
        entity.softDelete();
        repository.save(entity);
    }

    private EmergencyContact getEntityById(Long id) {
        return repository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmergencyContact not found"));
    }

    private void copyDtoToEntity(EmergencyContactDTO dto, EmergencyContact entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRelationship(Relationship.valueOf(dto.getRelationship()));
        entity.setClient(clientRepository.findByIdActive(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found")));
    }

}
