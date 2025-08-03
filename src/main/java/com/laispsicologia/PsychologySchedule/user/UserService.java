package com.laispsicologia.PsychologySchedule.user;

import com.laispsicologia.PsychologySchedule.exceptions.AlreadyExistingUsernameException;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.user.dto.UserRegisterDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserReturnDTO;
import com.laispsicologia.PsychologySchedule.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserReturnDTO registerUser(UserRegisterDTO registerDto) {
        if (repository.verifyUsername(registerDto.getEmail()))
            throw new AlreadyExistingUsernameException("This email is already used");

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = createNewUser(registerDto, encodedPassword);
        user = repository.save(user);

        return new UserReturnDTO(user);
    }

    private User createNewUser(UserRegisterDTO dto, String passwordEncoded) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoded);
        user.setRole(dto.getRole());

        return user;
    }
}
