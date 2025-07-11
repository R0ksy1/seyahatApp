package com.roksy.seyahatuygulamasi.common.dto;

import java.util.Date;

public record ResponsePlanDto(Long id, String planAdi, String sehir, Date baslangicTarih, Date bitisTarih) {
}
