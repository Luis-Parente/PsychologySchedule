package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.laispsicologia.PsychologySchedule.emergencyContact.entity.EmergencyContact;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.Relationship;
import com.laispsicologia.PsychologySchedule.factory.EmergencyContactFactory;
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
public class EmergencyContactRepositoryTests {

    @Autowired
    private EmergencyContactRepository repository;

    private Long validId;
    private Long invalidId;
    private Long countTotalContacts;

    Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2000L;
        countTotalContacts = 2L;

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void findAllActiveShouldReturnPageOfEmergencyContact() {
        Page<EmergencyContact> result = repository.findAllActive(pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalContacts, result.getTotalElements());
    }

    @Test
    public void findByIdActiveShouldReturnNotEmptyOptionalWhenValidIdAndDeletedAtIsNull() {
        Optional<EmergencyContact> result = repository.findByIdActive(validId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(validId, result.get().getId());
        Assertions.assertEquals("Douglas Souza", result.get().getName());
        Assertions.assertEquals("douglas@gmail.com", result.get().getEmail());
        Assertions.assertEquals("93214-3214", result.get().getPhoneNumber());
        Assertions.assertEquals(Relationship.SIBLING, result.get().getRelationship());
        Assertions.assertEquals(1L, result.get().getClient().getId());
        Assertions.assertNull(result.get().getDeletedAt());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenInvalidId() {
        Optional<EmergencyContact> result = repository.findByIdActive(invalidId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenValidIdAndDeletedAtIsNotNull() {
        Optional<EmergencyContact> result = repository.findByIdActive(validId);
        result.get().softDelete();
        repository.save(result.get());
        result = repository.findByIdActive(validId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistEmergencyContactWithAutoIncrementWhenIdIsNull() {
        EmergencyContact contact = EmergencyContactFactory.createEmergencyContact();
        contact.setId(null);
        EmergencyContact result = repository.save(contact);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(countTotalContacts + 1, result.getId());
        Assertions.assertEquals("Marcio Lemes", result.getName());
        Assertions.assertEquals("marcio@gmail.com", result.getEmail());
        Assertions.assertEquals("11973634545", result.getPhoneNumber());
        Assertions.assertEquals(Relationship.PARENT, result.getRelationship());
        Assertions.assertEquals(1L, result.getClient().getId());
        Assertions.assertNull(result.getDeletedAt());
    }

    @Test
    public void saveShouldUpdateEmergencyContactWithAutoIncrementWhenValidId() {
        EmergencyContact contact = EmergencyContactFactory.createEmergencyContact();
        contact.setId(validId);
        EmergencyContact result = repository.save(contact);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals("Marcio Lemes", result.getName());
        Assertions.assertEquals("marcio@gmail.com", result.getEmail());
        Assertions.assertEquals("11973634545", result.getPhoneNumber());
        Assertions.assertEquals(Relationship.PARENT, result.getRelationship());
        Assertions.assertEquals(1L, result.getClient().getId());
        Assertions.assertNull(result.getDeletedAt());
    }
}
