package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.laispsicologia.PsychologySchedule.client.ClientRepository;
import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.Relationship;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.EmergencyContactFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class EmergencyContactServiceIT {

    @Autowired
    private EmergencyContactService service;

    @Autowired
    private EmergencyContactRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    private Long validContactId, invalidContactId;

    private Long invalidClientId;

    private Long countTotalContacts;

    private LocalDateTime validDate, invalidDate;

    private Pageable pageable;

    private EmergencyContactDTO dto;

    private String expectedName, expectedEmail, expectedPhoneNumber, expectedRelationship;

    private Long expectedClientId;

    @BeforeEach
    void setUp() throws Exception {
        validContactId = 1L;
        invalidContactId = 1000L;

        countTotalContacts = 2L;

        invalidClientId = 1000L;

        pageable = PageRequest.of(0, 10);

        dto = EmergencyContactFactory.createEmergencyContactDto();

        expectedName = "Douglas Souza";
        expectedEmail = "douglas@gmail.com";
        expectedPhoneNumber = "93214-3214";
        expectedRelationship = Relationship.SIBLING.toString();
        expectedClientId = 1L;
    }

    @Test
    public void findAllShouldReturnPageOfEmergencyContactDto() {
        Page<EmergencyContactDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(validContactId, result.getContent().get(0).getId());
        Assertions.assertEquals(expectedName, result.getContent().get(0).getName());
        Assertions.assertEquals(expectedEmail, result.getContent().get(0).getEmail());
        Assertions.assertEquals(expectedPhoneNumber, result.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals(expectedRelationship, result.getContent().get(0).getRelationship());
        Assertions.assertEquals(expectedClientId, result.getContent().get(0).getClientId());
    }

    @Test
    public void findByIdShouldReturnEmergencyContactDTOWhenValidEmergencyContactIdAndDeletedAtIsNull() {
        EmergencyContactDTO result = service.findById(validContactId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validContactId, result.getId());
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedEmail, result.getEmail());
        Assertions.assertEquals(expectedPhoneNumber, result.getPhoneNumber());
        Assertions.assertEquals(expectedRelationship, result.getRelationship());
        Assertions.assertEquals(expectedClientId, result.getClientId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenInvalidEmergencyContactId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(invalidContactId);
        });
    }

    @Test
    public void insertShouldReturnEmergencyContactDtoWhenValidClientId() {
        dto.setId(null);
        EmergencyContactDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(countTotalContacts + 1, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(dto.getRelationship(), result.getRelationship());
        Assertions.assertEquals(dto.getClientId(), result.getClientId());
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
        EmergencyContactDTO result = service.update(validContactId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validContactId, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(dto.getRelationship(), result.getRelationship());
        Assertions.assertEquals(dto.getClientId(), result.getClientId());
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
