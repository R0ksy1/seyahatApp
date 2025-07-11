package com.roksy.seyahatuygulamasi.service.impl;

import com.roksy.seyahatuygulamasi.common.dto.CreatePlanDto;
import com.roksy.seyahatuygulamasi.common.dto.ResponsePlanDto;
import com.roksy.seyahatuygulamasi.common.dto.UpdatePlanDto;
import com.roksy.seyahatuygulamasi.data.jpa.PlanEntity;
import com.roksy.seyahatuygulamasi.data.jpa.UserEntity;
import com.roksy.seyahatuygulamasi.data.repository.PlanRepository;
import com.roksy.seyahatuygulamasi.data.repository.UserRepository;
import com.roksy.seyahatuygulamasi.service.IPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService implements IPlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;


    @Override
    public ResponsePlanDto CreatePlan(CreatePlanDto createPlanDto, UserDetails userDetails) {
        String username = userDetails.getUsername();

        UserEntity planinSahibi = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Token sahibi kullanıcı veritabanında bulunamadı."));

        PlanEntity newPlan = new PlanEntity();
        newPlan.setPlanAdi(createPlanDto.planAdi());
        newPlan.setSehir(createPlanDto.sehir());
        newPlan.setBaslangicTarih(createPlanDto.baslangicTarih());
        newPlan.setBitisTarih(createPlanDto.bitisTarih());

        newPlan.setUser(planinSahibi);

        PlanEntity kaydedilmisPlan = planRepository.save(newPlan);

        return new ResponsePlanDto(
                kaydedilmisPlan.getId(),
                kaydedilmisPlan.getPlanAdi(),
                kaydedilmisPlan.getSehir(),
                kaydedilmisPlan.getBaslangicTarih(),
                kaydedilmisPlan.getBitisTarih()
        );
    }

    @Override
    public List<ResponsePlanDto> GetAllPlans(UserDetails userDetails) {
        String username = userDetails.getUsername();

        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı."));

        List<PlanEntity> userPlans = planRepository.findAllByUser(currentUser);

        return userPlans.stream()
                .map(plan -> new ResponsePlanDto(
                        plan.getId(),
                        plan.getPlanAdi(),
                        plan.getSehir(),
                        plan.getBaslangicTarih(),
                        plan.getBitisTarih()))
                .toList();
    }

    @Override
    public ResponsePlanDto GetPlanById(Long planId, UserDetails userDetails) {
        PlanEntity plan = getPlanByIdAndCheckOwnership(planId, userDetails);
        return mapToPlanDto(plan);
    }


    @Override
    public void deletePlan(Long planId, UserDetails userDetails) {
        getPlanByIdAndCheckOwnership(planId, userDetails);

        planRepository.deleteById(planId);
    }

    @Override
    public ResponsePlanDto updatePlan(Long planId, UpdatePlanDto updatePlanDto, UserDetails userDetails) {
        PlanEntity existingPlan = getPlanByIdAndCheckOwnership(planId, userDetails);

        if (updatePlanDto.planAdi() != null) {
            existingPlan.setPlanAdi(updatePlanDto.planAdi());
        }
        if (updatePlanDto.sehir() != null) {
            existingPlan.setSehir(updatePlanDto.sehir());
        }
        if (updatePlanDto.baslangicTarih() != null) {
            existingPlan.setBaslangicTarih(updatePlanDto.baslangicTarih());
        }
        if (updatePlanDto.bitisTarih() != null) {
            existingPlan.setBitisTarih(updatePlanDto.bitisTarih());
        }

        PlanEntity updatedPlan = planRepository.save(existingPlan);

        return mapToPlanDto(updatedPlan);
    }

    private PlanEntity getPlanByIdAndCheckOwnership(Long planId, UserDetails userDetails) {
        String username = userDetails.getUsername();

        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan bulunamadı. ID: " + planId));

        if (!plan.getUser().getUsername().equals(username)) {
            throw new SecurityException("Bu plana erişim yetkiniz yok.");
        }

        return plan;
    }

    private ResponsePlanDto mapToPlanDto(PlanEntity planEntity) {
        return new ResponsePlanDto(
                planEntity.getId(),
                planEntity.getPlanAdi(),
                planEntity.getSehir(),
                planEntity.getBaslangicTarih(),
                planEntity.getBitisTarih()
        );
    }

}
