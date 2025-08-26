package com.laispsicologia.PsychologySchedule.professional;

import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.ProfessionalFactory;
import com.laispsicologia.PsychologySchedule.professional.dto.ProfessionalDTO;
import com.laispsicologia.PsychologySchedule.professional.entity.Professional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ProfessionalServiceIT {

    @Autowired
    private ProfessionalService service;

    private Long validId, invalidId;

    private Professional professional;

    private ProfessionalDTO dto;

    private PageImpl<Professional> page;

    private Pageable pageable;

    private Long expectedId;

    private String expectedName, expectedRegistrationNumber, expectedEmail, expectedPhoneNumber;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        professional = ProfessionalFactory.createProfessional();

        dto = ProfessionalFactory.createProfessionalDTO();

        page = new PageImpl<>(List.of(professional));

        pageable = PageRequest.of(0, 10);

        expectedId = 1L;
        expectedName = "Mauro Silva";
        expectedRegistrationNumber = "CRP SP/123456";
        expectedEmail = "mauro@gmail.com";
        expectedPhoneNumber = "91234-1234";
    }

    @Test
    public void findAllShouldReturnPageOfProfessionalDTO() {
        Page<ProfessionalDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(expectedId, result.getContent().get(0).getId());
        Assertions.assertEquals(expectedName, result.getContent().get(0).getName());
        Assertions.assertEquals(expectedRegistrationNumber,
                result.getContent().get(0).getRegistrationNumber());
        Assertions.assertEquals(expectedEmail, result.getContent().get(0).getEmail());
        Assertions.assertEquals(expectedPhoneNumber, result.getContent().get(0).getPhoneNumber());
    }

    @Test
    public void findByIdShouldReturnProfessionalDTOWhenValidId() {
        ProfessionalDTO result = service.findById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedRegistrationNumber, result.getRegistrationNumber());
        Assertions.assertEquals(expectedEmail, result.getEmail());
        Assertions.assertEquals(expectedPhoneNumber, result.getPhoneNumber());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(invalidId);
        });
    }

    @Test
    public void insertShouldReturnProfessionalDTO() {
        ProfessionalDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getRegistrationNumber(), result.getRegistrationNumber());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void updateShouldReturnProfessionalDTOWhenValidId() {
        ProfessionalDTO result = service.update(validId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getRegistrationNumber(), result.getRegistrationNumber());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void updateShouldThrowResourceNotFoundWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(invalidId, dto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenValidId() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(validId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenInvalidId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(invalidId);
        });
    }
}
