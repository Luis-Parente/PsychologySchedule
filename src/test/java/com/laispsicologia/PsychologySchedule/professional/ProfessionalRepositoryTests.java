package com.laispsicologia.PsychologySchedule.professional;

import com.laispsicologia.PsychologySchedule.factory.ProfessionalFactory;
import com.laispsicologia.PsychologySchedule.professional.entity.Professional;
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
public class ProfessionalRepositoryTests {
    @Autowired
    private ProfessionalRepository repository;

    private Long validId;
    private Long invalidId;
    private Long countTotalProfessionals;

    Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2000L;
        countTotalProfessionals = 1L;

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void findAllActiveShouldReturnPageOfProfessional() {
        Page<Professional> result = repository.findAllActive(pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProfessionals, result.getTotalElements());
    }

    @Test
    public void findByIdActiveShouldReturnNotEmptyOptionalWhenValidIdAndDeletedAtIsNull() {
        Optional<Professional> result = repository.findByIdActive(validId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(validId, result.get().getId());
        Assertions.assertEquals("Mauro Silva", result.get().getName());
        Assertions.assertEquals("CRP SP/123456", result.get().getRegistrationNumber());
        Assertions.assertEquals("mauro@gmail.com", result.get().getEmail());
        Assertions.assertEquals("91234-1234", result.get().getPhoneNumber());
        Assertions.assertNull(result.get().getDeletedAt());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenInvalidId() {
        Optional<Professional> result = repository.findByIdActive(invalidId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdActiveShouldReturnEmptyOptionalWhenValidIdAndDeletedAtIsNotNull() {
        Optional<Professional> result = repository.findByIdActive(validId);
        result.get().softDelete();
        repository.save(result.get());
        result = repository.findByIdActive(validId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistProfessionalWithAutoIncrementWhenIdIsNull() {
        Professional professional = ProfessionalFactory.createProfessional();
        professional.setId(null);
        Professional result = repository.save(professional);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(countTotalProfessionals + 1, result.getId());
        Assertions.assertEquals("Carla Maria", result.getName());
        Assertions.assertEquals("CRP-RJ 123.123", result.getRegistrationNumber());
        Assertions.assertEquals("carla@gmail.com", result.getEmail());
        Assertions.assertEquals("12 1234-1234", result.getPhoneNumber());
        Assertions.assertNull(result.getDeletedAt());
    }

    @Test
    public void saveShouldUpdateProfessionalWhenValidId() {
        Professional professional = ProfessionalFactory.createProfessional();
        professional.setId(validId);
        Professional result = repository.save(professional);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals("Carla Maria", result.getName());
        Assertions.assertEquals("CRP-RJ 123.123", result.getRegistrationNumber());
        Assertions.assertEquals("carla@gmail.com", result.getEmail());
        Assertions.assertEquals("12 1234-1234", result.getPhoneNumber());
        Assertions.assertNull(result.getDeletedAt());
    }
}
