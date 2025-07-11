package com.roksy.seyahatuygulamasi.service;

import com.roksy.seyahatuygulamasi.common.dto.CreatePlanDto;
import com.roksy.seyahatuygulamasi.common.dto.ResponsePlanDto;
import com.roksy.seyahatuygulamasi.common.dto.UpdatePlanDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IPlanService {

    ResponsePlanDto CreatePlan(CreatePlanDto createPlanDto, UserDetails userDetails);

    List<ResponsePlanDto> GetAllPlans(UserDetails userDetails);

    ResponsePlanDto GetPlanById(Long id, UserDetails userDetails);

    ResponsePlanDto updatePlan(Long id, UpdatePlanDto updatePlanDto, UserDetails userDetails);

    void deletePlan(Long id, UserDetails userDetails);
}
