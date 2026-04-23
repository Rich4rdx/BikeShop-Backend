package com.example.tienda_bicicletas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DetalleVentaResponseDto {
    private String marcaBicicleta;
    private String modeloBicicleta;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
