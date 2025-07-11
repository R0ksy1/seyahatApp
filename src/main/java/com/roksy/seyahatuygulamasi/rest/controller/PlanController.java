package com.roksy.seyahatuygulamasi.rest.controller;

import com.roksy.seyahatuygulamasi.common.dto.CreatePlanDto;
import com.roksy.seyahatuygulamasi.common.dto.ResponsePlanDto;
import com.roksy.seyahatuygulamasi.common.dto.UpdatePlanDto;
import com.roksy.seyahatuygulamasi.service.impl.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans") // Bu controller'daki tüm adresler /api/v1/plans ile başlayacak
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<ResponsePlanDto> createPlan(
            @RequestBody CreatePlanDto createPlanDto,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        ResponsePlanDto createdPlan = planService.CreatePlan(createPlanDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePlanDto>> getAllPlans(
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        List<ResponsePlanDto> plans = planService.GetAllPlans(currentUser);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<ResponsePlanDto> getPlanById(
            @PathVariable Long planId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        ResponsePlanDto plan = planService.GetPlanById(planId, currentUser);
        return ResponseEntity.ok(plan);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<ResponsePlanDto> updatePlan(
            @PathVariable Long planId,
            @RequestBody UpdatePlanDto updatePlanDto,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        ResponsePlanDto updatedPlan = planService.updatePlan(planId, updatePlanDto, currentUser);
        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long planId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        planService.deletePlan(planId, currentUser);
        return ResponseEntity.noContent().build(); // 204 No Content yanıtı, başarılı silme işlemleri için standarttır.
    }
}