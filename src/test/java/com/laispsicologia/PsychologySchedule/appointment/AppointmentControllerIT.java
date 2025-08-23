package com.laispsicologia.PsychologySchedule.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.appointment.entity.Appointment;
import com.laispsicologia.PsychologySchedule.appointment.entity.AppointmentStatus;
import com.laispsicologia.PsychologySchedule.factory.AppointmentFactory;
import com.laispsicologia.PsychologySchedule.utils.TokenUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppointmentControllerIT {

    private static final String BASE_URL = "/appointments";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Long validId, invalidId;

    private Long invalidClientId;

    private PageImpl<AppointmentDTO> page;

    private AppointmentDTO dto;

    private AppointmentMinDTO minDto;

    private String email, password, bearerToken;

    private String expectedFirstDate, expectedLastDate, expectedAppointmentStatus, expectedClientName;

    private Double expectedPrice;

    private Boolean expectedPaid;

    private Long expectedClientId;

    private Appointment appointment;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        invalidClientId = 1000L;

        dto = AppointmentFactory.createAppointmentDto();

        minDto = AppointmentFactory.createAppointmentMinDto();

        page = new PageImpl<>(List.of(dto));

        email = "paulo@gmail.com";
        password = "123456789";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, email, password);

        expectedFirstDate = "2025-08-01T13:00:00";
        expectedLastDate = "2025-08-01T14:00:00";
        expectedAppointmentStatus = AppointmentStatus.PENDING.toString();
        expectedPrice = 80.0;
        expectedPaid = false;
        expectedClientId = 1L;
        expectedClientName = "Lucas Souza";

        appointment = AppointmentFactory.createAppointment();
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfAppointment() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", bearerToken)
                        .param("firstDate", String.valueOf(LocalDate.now().minusDays(15)))
                        .param("lastDate", String.valueOf(LocalDate.now().plusDays(15)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnAppointmentDtoWhenValidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.startTime").value(expectedFirstDate))
                .andExpect(jsonPath("$.endTime").value(expectedLastDate))
                .andExpect(jsonPath("$.appointmentStatus").value(expectedAppointmentStatus))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.paid").value(expectedPaid))
                .andExpect(jsonPath("$.clientId").value(expectedClientId))
                .andExpect(jsonPath("$.clientName").value(expectedClientName));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnAppointmentDtoWhenValidDateAndValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.startTime").value(
                        Matchers.startsWith(dto.getStartTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.endTime").value(
                        Matchers.startsWith(dto.getEndTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.appointmentStatus").value(dto.getAppointmentStatus()))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.paid").value(dto.getPaid()))
                .andExpect(jsonPath("$.clientId").value(expectedClientId))
                .andExpect(jsonPath("$.clientName").value(expectedClientName));
    }

    @Test
    public void insertShouldReturnNotFoundWhenValidDateAndInvalidClientId() throws Exception {
        minDto.setClientId(invalidClientId);
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnConflictWhenInvalidDateAndValidClientId() throws Exception {
        appointmentRepository.save(appointment);
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenStartTimeIsPast() throws Exception {
        minDto.setStartTime(LocalDateTime.now().minusDays(1));
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].fieldName").value("startTime"))
                .andExpect(jsonPath("$.errors[0].message").value("Date must be a future date"));
    }

    @Test
    public void updateShouldReturnAppointmentDtoWhenValidAppointmentIdAndValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.startTime").value(
                        Matchers.startsWith(dto.getStartTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.endTime").value(
                        Matchers.startsWith(dto.getEndTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.appointmentStatus").value(dto.getAppointmentStatus()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()))
                .andExpect(jsonPath("$.paid").value(dto.getPaid()))
                .andExpect(jsonPath("$.clientId").value(expectedClientId))
                .andExpect(jsonPath("$.clientName").value(expectedClientName));
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalidAppointmentId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalidClientId() throws Exception {
        dto.setClientId(invalidClientId);
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenValidId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNoContentWhenInalidId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
