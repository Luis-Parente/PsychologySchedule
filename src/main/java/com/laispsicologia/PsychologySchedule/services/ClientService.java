package com.laispsicologia.PsychologySchedule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.laispsicologia.PsychologySchedule.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.entities.Client;
import com.laispsicologia.PsychologySchedule.repositories.ClientRepository;
import com.laispsicologia.PsychologySchedule.service.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	public Page<ClientDTO> findAll(Pageable pageable) {
		return repository.findAllActive(pageable).map(entity -> new ClientDTO(entity));
	}

	public ClientDTO findById(Long id) {
		Client entity = getEntityById(id);
		return new ClientDTO(entity);
	}

	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);

		return new ClientDTO(entity);
	}

	public ClientDTO update(Long id, ClientDTO dto) {
		Client entity = getEntityById(id);
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);

		return new ClientDTO(entity);
	}

	public void delete(Long id) {
		Client entity = getEntityById(id);
		entity.softDelete();
		repository.save(entity);
	}

	private Client getEntityById(Long id) {
		return repository.findByIdActive(id).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
	}

	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setBirthDate(dto.getBirthDate());
		entity.setEmail(dto.getEmail());
		entity.setPhoneNumber(dto.getPhoneNumber());
	}

}
