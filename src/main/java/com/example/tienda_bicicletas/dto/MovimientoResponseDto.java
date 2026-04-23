package com.example.tienda_bicicletas.dto;

import com.example.tienda_bicicletas.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoResponseDto {
    private Long id;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private LocalDate fecha;
    private Long codigoBicicleta;
    private String marcaBicicleta;
    private String modeloBicicleta;
}
