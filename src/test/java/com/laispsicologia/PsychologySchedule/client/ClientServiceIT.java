package com.laispsicologia.PsychologySchedule.client;

import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.dto.ClientMinDTO;
import com.laispsicologia.PsychologySchedule.factory.ClientFactory;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ClientServiceIT {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    private Long existingId;
    private Long invalidId;

    private String validName;
    private String invalidName;

    private ClientDTO dto;

    private Long countTotalClients;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        invalidId = 2000L;

        validName = "Lucas";
        invalidName = "Luis";

        dto = ClientFactory.createClientDto();

        countTotalClients = 2L;
    }

    @Test
    public void findAllShouldReturnPageOfClient() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientMinDTO> result = service.findAll(pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.getContent().size() <= pageable.getPageSize());
        Assertions.assertEquals(countTotalClients, result.getTotalElements());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findByNameShouldReturnPageOfClientWhenNameExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientMinDTO> result = service.findByName(validName, pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.getContent().stream()
                .allMatch(client -> client.getName().toLowerCase().contains(validName.toLowerCase())));
        Assertions.assertTrue(result.getContent().size() <= pageable.getPageSize());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findByNameShouldReturnPageEmptyWhenNameDoesNotExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientMinDTO> result = service.findByName(invalidName, pageable);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.getTotalElements());
        Assertions.assertEquals(0, result.getContent().size());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findByIdShouldReturnClientDTOWhenIdExists() {
        ClientDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Lucas Souza", result.getName());
        Assertions.assertEquals("123.123.123-12", result.getCpf());
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(invalidId);
        });
    }

    @Test
    public void insertShouldReturnClientDto() {
        ClientDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(countTotalClients + 1, result.getId());
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
            service.update(invalidId, dto);
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
            service.delete(invalidId);
        });
    }
}
