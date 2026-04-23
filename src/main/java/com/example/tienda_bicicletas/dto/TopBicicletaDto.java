package com.example.tienda_bicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TopBicicletaDto {
    private String marca;
    private String modelo;
    private int cantidadVendida;
    private BigDecimal ingresosTotales;
}
