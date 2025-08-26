package com.laispsicologia.PsychologySchedule.professional;

import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.ProfessionalFactory;
import com.laispsicologia.PsychologySchedule.professional.dto.ProfessionalDTO;
import com.laispsicologia.PsychologySchedule.professional.entity.Professional;
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
public class ProfessionalServiceTests {

    @InjectMocks
    private ProfessionalService service;

    @Mock
    private ProfessionalRepository repository;

    private Long validId, invalidId;

    private Professional professional;

    private ProfessionalDTO dto;

    private PageImpl<Professional> page;

    private Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        professional = ProfessionalFactory.createProfessional();

        dto = ProfessionalFactory.createProfessionalDTO();

        page = new PageImpl<>(List.of(professional));

        pageable = PageRequest.of(0, 10);

        Mockito.when(repository.findAllActive(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        Mockito.when(repository.findByIdActive(validId)).thenReturn(Optional.of(professional));
        Mockito.when(repository.findByIdActive(invalidId)).thenReturn(Optional.empty());

        Mockito.when(repository.save(ArgumentMatchers.any(Professional.class))).thenReturn(professional);
    }

    @Test
    public void findAllShouldReturnPageOfProfessionalDTO() {
        Page<ProfessionalDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(professional.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(professional.getName(), result.getContent().get(0).getName());
        Assertions.assertEquals(professional.getRegistrationNumber(),
                result.getContent().get(0).getRegistrationNumber());
        Assertions.assertEquals(professional.getEmail(), result.getContent().get(0).getEmail());
        Assertions.assertEquals(professional.getPhoneNumber(), result.getContent().get(0).getPhoneNumber());
    }

    @Test
    public void findByIdShouldReturnProfessionalDTOWhenValidId() {
        ProfessionalDTO result = service.findById(validId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(professional.getId(), result.getId());
        Assertions.assertEquals(professional.getName(), result.getName());
        Assertions.assertEquals(professional.getRegistrationNumber(), result.getRegistrationNumber());
        Assertions.assertEquals(professional.getEmail(), result.getEmail());
        Assertions.assertEquals(professional.getPhoneNumber(), result.getPhoneNumber());
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
        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getRegistrationNumber(), result.getRegistrationNumber());
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
        Assertions.assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void updateShouldReturnProfessionalDTOWhenValidId() {
        ProfessionalDTO result = service.update(validId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.getId(), result.getId());
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
