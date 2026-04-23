package com.example.tienda_bicicletas.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProveedorResponseDto {

    private String nit;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String ciudad;
}

