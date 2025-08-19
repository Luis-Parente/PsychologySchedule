package com.laispsicologia.PsychologySchedule.client;

import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.client.testutils.ClientFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository repository;

    private Long existingId;
    private Long invalidId;
    private Long countTotalClients;

    private String validName;
    private String invalidName;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        invalidId = 2000L;
        countTotalClients = 2L;

        validName = "Lucas";
        invalidName = "Luis";
    }

    @Test
    public void saveShouldPersistClientWithAutoIncrementWhenIdIsNull() {
        Client client = ClientFactory.createClient();
        client.setId(null);

        client = repository.save(client);

        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.getId());
        Assertions.assertEquals(countTotalClients + 1, client.getId());
    }

    @Test
    public void saveShouldPersistClientWhenIdExists() {
        Client client = ClientFactory.createClient();
        client.setId(existingId);

        client = repository.save(client);

        Assertions.assertNotNull(client);
        Assertions.assertEquals(existingId, client.getId());
    }

    @Test
    public void findAllActiveShouldReturnPageOfClient() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> result = repository.findAllActive(pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
    }

    @Test
    public void findByIdActiveShouldReturnNotEmptyOptionalWhenValidIdAndDeletedAtIsNull() {
        Optional<Client> result = repository.findByIdActive(existingId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(existingId, result.get().getId());
        Assertions.assertEquals("Lucas Souza", result.get().getName());
        Assertions.assertEquals("123.123.123-12", result.get().getCpf());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenInvalidId() {
        Optional<Client> result = repository.findByIdActive(invalidId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenValidIdAndDeletedAtIsNotNull() {
        Optional<Client> result = repository.findByIdActive(existingId);
        result.get().softDelete();
        repository.save(result.get());
        result = repository.findByIdActive(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findClientWithContactsShouldReturnNotEmptyOptionalWhenValidIdAndDeletedAtIsNull() {
        Optional<Client> result = repository.findClientWithContacts(existingId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(existingId, result.get().getId());
        Assertions.assertFalse(result.get().getContacts().isEmpty());
    }

    @Test
    public void findClientWithContactsShouldReturnEmptyOptionalWhenInvalidId() {
        Optional<Client> result = repository.findClientWithContacts(invalidId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findClientWithContactsShouldReturnEmptyOptionalWhenValidIdAndDeletedAtIsNotNull() {
        Optional<Client> result = repository.findClientWithContacts(existingId);
        result.get().softDelete();
        repository.save(result.get());
        result = repository.findByIdActive(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByNameShouldReturnPageOfClientWhenValidName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> result = repository.findByName(validName, pageable);

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByNameShouldReturnEmptyPageOfClientWhenInvalidName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> result = repository.findByName(invalidName, pageable);

        Assertions.assertTrue(result.isEmpty());
    }
}
