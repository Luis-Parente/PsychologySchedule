package com.laispsicologia.PsychologySchedule.user;

import com.laispsicologia.PsychologySchedule.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_user WHERE email = :username")
    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM tb_user WHERE email = :username)")
    Boolean verifyUsername(String username);
}
