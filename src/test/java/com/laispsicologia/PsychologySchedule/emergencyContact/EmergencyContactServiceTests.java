package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.client.entity.Client;
import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.ClientFactory;
import com.laispsicologia.PsychologySchedule.factory.EmergencyContactFactory;
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
public class EmergencyContactServiceTests {

    @InjectMocks
    private EmergencyContactService service;

    @Mock
    private EmergencyContactRepository repository;

    @Mock
    private ClientRepository clientRepository;

    private Long validContactId, invalidContactId;

    private Long validClientId, invalidClientId;

    private PageImpl<EmergencyContact> page;

    private EmergencyContact contact;

    private EmergencyContactDTO dto;

    private Client client;

    private Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        validContactId = 1L;
        invalidContactId = 1000L;

        validClientId = 1L;
        invalidClientId = 1000L;

        contact = EmergencyContactFactory.createEmergencyContact();

        dto = EmergencyContactFactory.createEmergencyContactDto();

        client = ClientFactory.createClient();

        page = new PageImpl<>(List.of(contact));

        pageable = PageRequest.of(0, 10);

        Mockito.when(repository.findAllActive(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        Mockito.when(repository.findByIdActive(validContactId)).thenReturn(Optional.of(contact));
        Mockito.when(repository.findByIdActive(invalidContactId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any(EmergencyContact.class))).thenReturn(contact);

        Mockito.when(clientRepository.findByIdActive(validClientId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findByIdActive(invalidClientId)).thenReturn(Optional.empty());
    }

    @Test
    public void findAllShouldReturnPageOfEmergencyContactDto() {
        Page<EmergencyContactDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(contact.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(contact.getName(), result.getContent().get(0).getName());
        Assertions.assertEquals(contact.getEmail(), result.getContent().get(0).getEmail());
        Assertions.assertEquals(contact.getPhoneNumber(), result.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals(contact.getRelationship().toString(), result.getContent().get(0).getRelationship());
        Assertions.assertEquals(contact.getClient().getId(), result.getContent().get(0).getClientId());
    }

    @Test
    public void findByIdShouldReturnEmergencyContactDTOWhenValidEmergencyContactIdAndDeletedAtIsNull() {
        EmergencyContactDTO result = service.findById(validContactId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(contact.getId(), result.getId());
        Assertions.assertEquals(contact.getName(), result.getName());
        Assertions.assertEquals(contact.getEmail(), result.getEmail());
        Assertions.assertEquals(contact.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(contact.getRelationship().toString(), result.getRelationship());
        Assertions.assertEquals(contact.getClient().getId(), result.getClientId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenInvalidEmergencyContactId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(invalidContactId);
        });
    }

    @Test
    public void insertShouldReturnEmergencyContactDtoWhenValidClientId() {
        dto.setClientId(validClientId);
        EmergencyContactDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(contact.getId(), result.getId());
        Assertions.assertEquals(contact.getName(), result.getName());
        Assertions.assertEquals(contact.getEmail(), result.getEmail());
        Assertions.assertEquals(contact.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(contact.getRelationship().toString(), result.getRelationship());
        Assertions.assertEquals(contact.getClient().getId(), result.getClientId());
    }

    @Test
    public void insertShouldThrowResourceNotFoundWhenInvalidClientId() {
        dto.setClientId(invalidClientId);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.insert(dto);
        });
    }

    @Test
    public void updateShouldReturnEmergencyContactDTOWhenValidEmergencyContactIdAndValidClientId() {
        dto.setClientId(validClientId);
        EmergencyContactDTO result = service.update(validContactId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validContactId, result.getId());
        Assertions.assertEquals(contact.getName(), result.getName());
        Assertions.assertEquals(contact.getEmail(), result.getEmail());
        Assertions.assertEquals(contact.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(contact.getRelationship().toString(), result.getRelationship());
        Assertions.assertEquals(contact.getClient().getId(), result.getClientId());
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenInvalidEmergencyContactId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(invalidContactId, dto);
        });
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenValidEmergencyContactIdAndInvalidClientId() {
        dto.setClientId(invalidClientId);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(validContactId, dto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenValidId() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(validContactId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(invalidContactId);
        });
    }
}
