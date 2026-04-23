package com.example.tienda_bicicletas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class HistorialComprasResponseDto {

    private Long id;
    private String nitProveedor;
    private String nombreProveedor;
    private Long codigoBicicleta;
    private String marcaBicicleta;
    private String modeloBicicleta;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal totalCompra;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCompra;

    private String notas;
}