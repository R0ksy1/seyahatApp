package com.roksy.seyahatuygulamasi.common.dto;

import java.util.Date;

public record CreatePlanDto(String planAdi, String sehir, Date baslangicTarih, Date bitisTarih) {
}
