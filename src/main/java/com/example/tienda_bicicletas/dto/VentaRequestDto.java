package com.example.tienda_bicicletas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class VentaRequestDto {

    @NotBlank(message = "el documento del cliente es obligatorio ")
    private String documentoCliente;


    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Valid
    private List<DetalleVentaRequestDto> detalles;
}
