package com.laispsicologia.PsychologySchedule.client;

import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.dto.ClientMinDTO;
import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.factory.ClientFactory;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    private Long existingId, nonExistingId;

    private String validName, invalidName;

    private PageImpl<Client> page;

    private Client client;

    private ClientDTO dto;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        validName = "Nelson";
        invalidName = "Carlos";

        client = ClientFactory.createClient();

        dto = ClientFactory.createClientDto();

        page = new PageImpl<>(List.of(client));

        Mockito.when(repository.findAllActive(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        Mockito.when(repository.findByName(ArgumentMatchers.eq(validName), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);
        Mockito.when(repository.findByName(ArgumentMatchers.eq(invalidName), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        Mockito.when(repository.findClientWithContacts(existingId)).thenReturn(Optional.of(client));
        Mockito.when(repository.findClientWithContacts(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(client);
    }

    @Test
    public void findAllShouldReturnPageOfClient() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientMinDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(client.getName(), result.getContent().get(0).getName());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(1, result.getSize());
        Mockito.verify(repository, Mockito.times(1)).findAllActive(pageable);
    }

    @Test
    public void findByNameShouldReturnPageOfClientWhenNameExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientMinDTO> result = service.findByName(validName, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(client.getName(), result.getContent().get(0).getName());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(1, result.getSize());
        Mockito.verify(repository, Mockito.times(1)).findByName(validName, pageable);
    }

    @Test
    public void findByNameShouldReturnPageEmptyWhenNameDoesNotExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientMinDTO> result = service.findByName(invalidName, pageable);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.getTotalElements());
        Assertions.assertEquals(0, result.getContent().size());
        Assertions.assertTrue(result.getContent().isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(0, result.getSize());
        Mockito.verify(repository, Mockito.times(1)).findByName(invalidName, pageable);
    }

    @Test
    public void findByIdShouldReturnClientDTOWhenIdExists() {
        ClientDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getCpf(), result.getCpf());
        Assertions.assertEquals(dto.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnClientDto() {
        ClientDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getCpf(), result.getCpf());
        Assertions.assertEquals(dto.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void updateShouldReturnClientDtoWhenIdExists() {
        ClientDTO result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getCpf(), result.getCpf());
        Assertions.assertEquals(dto.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}
