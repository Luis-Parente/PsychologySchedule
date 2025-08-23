package com.laispsicologia.PsychologySchedule.user;

import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.config.security.token.TokenService;
import com.laispsicologia.PsychologySchedule.config.security.token.dto.LoginResponseDTO;
import com.laispsicologia.PsychologySchedule.exceptions.dto.CustomErrorDTO;
import com.laispsicologia.PsychologySchedule.exceptions.dto.ValidationErrorDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserLoginDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserRegisterDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserReturnDTO;
import com.laispsicologia.PsychologySchedule.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "Users", description = "Controller for Users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService service;

    @Autowired
    private TokenService tokenService;

    @Operation(description = "Authenticates the user using email and password", summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserLoginDTO dto) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(),
                dto.getPassword());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generatedToken((User) auth.getPrincipal());
        return ResponseEntity.ok().body(new LoginResponseDTO(token));
    }

    @Operation(description = "Register new user", summary = "New user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New user registered successfully", content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = CustomErrorDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(implementation = ValidationErrorDTO.class)))})
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<UserReturnDTO> register(@RequestBody @Valid UserRegisterDTO dto) {
        UserReturnDTO returnDTO = service.registerUser(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(returnDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(returnDTO);
    }
}
