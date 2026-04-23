package com.example.tienda_bicicletas.dto;

import com.example.tienda_bicicletas.enums.TipoBicicleta;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BicicletaRequestDto {
    @NotBlank(message = "La marca no puede estar vacía")
    @Size(min = 2, max = 50, message = "La marca debe tener entre 2 y 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(min = 1, max = 50, message = "El modelo no puede exceder los 50 caracteres")
    private String modelo;

    @NotNull(message = "El tipo de bicicleta (MONTAÑA, RUTA, etc.) es obligatorio")
    private TipoBicicleta tipo;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    @NotNull(message = "La cantidad en inventario es obligatoria")
    @Min(value = 0, message = "El inventario no puede ser un número negativo")
    private Integer cantidadInventario;

    @NotBlank(message = "El NIT del proveedor es obligatorio")
    @Size(max = 20, message = "El NIT no puede exceder 20 caracteres")
    private String nitProveedor;
}
