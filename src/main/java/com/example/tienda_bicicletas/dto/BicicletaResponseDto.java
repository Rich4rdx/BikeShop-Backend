package com.example.tienda_bicicletas.dto;

import com.example.tienda_bicicletas.enums.TipoBicicleta;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BicicletaResponseDto {
    private Long codigo;
    private String marca;
    private String modelo;
    private TipoBicicleta tipo;
    private BigDecimal precio;
    private Integer cantidadInventario;
    private String nitProveedor;
    private String nombreProveedor;
}
