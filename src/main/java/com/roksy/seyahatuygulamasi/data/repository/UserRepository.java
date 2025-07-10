package com.roksy.seyahatuygulamasi.data.repository;

import com.roksy.seyahatuygulamasi.data.jpa.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
