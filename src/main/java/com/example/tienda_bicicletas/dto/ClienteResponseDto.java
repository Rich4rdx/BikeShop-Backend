package com.example.tienda_bicicletas.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponseDto {

    private String documento;
    private String nombre;
    private String telefono;
}
