package com.example.tienda_bicicletas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HistorialComprasRequestDto {

    @NotBlank(message = "El NIT del proveedor es obligatorio")
    private String nitProveedor;

    @NotNull(message = "El código de la bicicleta es obligatorio")
    private Long codigoBicicleta;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    private BigDecimal precioUnitario;

    @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
    private String notas;
}