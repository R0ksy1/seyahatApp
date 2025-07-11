package com.roksy.seyahatuygulamasi.data.repository;

import com.roksy.seyahatuygulamasi.data.jpa.PlanEntity;
import com.roksy.seyahatuygulamasi.data.jpa.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    List<PlanEntity> findAllByUser(UserEntity user);
}
