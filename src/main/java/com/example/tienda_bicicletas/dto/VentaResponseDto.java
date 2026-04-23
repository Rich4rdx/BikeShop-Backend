package com.example.tienda_bicicletas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VentaResponseDto {
    private Long idVenta;
    private LocalDateTime fecha;
    private String cliente;
    private BigDecimal total;
    private String documentoCliente;
    private String usuarioNombre;
    private List<DetalleVentaResponseDto> detalles;
}
