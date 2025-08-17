package com.laispsicologia.PsychologySchedule.client;

import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.client.testutils.ClientFactory;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    private Long existingId, nonExistingId;

    private Client client;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        client = ClientFactory.createClient();

        Mockito.when(repository.findClientWithContacts(existingId)).thenReturn(Optional.of(client));
        Mockito.when(repository.findClientWithContacts(nonExistingId)).thenReturn(Optional.empty());
    }

    @Test
    public void findByIdShouldReturnClientDTOWhenIdExists() {
        ClientDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), client.getName());
        Assertions.assertEquals(result.getCpf(), client.getCpf());
        Assertions.assertEquals(result.getBirthDate(), client.getBirthDate());
        Assertions.assertEquals(result.getEmail(), client.getEmail());
        Assertions.assertEquals(result.getPhoneNumber(), client.getPhoneNumber());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }
}
