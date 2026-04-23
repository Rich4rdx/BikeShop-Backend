package com.example.tienda_bicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VentaPorDiaDto {
    private LocalDate fecha;
    private int cantidadVentas;
    private BigDecimal totalDia;
}
