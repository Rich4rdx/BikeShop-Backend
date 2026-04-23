package com.example.tienda_bicicletas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ReporteVentasDto {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int totalVentas;
    private BigDecimal totalIngresos;
    private BigDecimal promedioPorVenta;
    private String clienteFrecuente;
    private int comprasClienteFrecuente;
    private List<TopBicicletaDto> topBicicletas;
    private List<VentaPorDiaDto> ventasPorDia;
    private List<VentaResponseDto> ventasRecientes;
}
