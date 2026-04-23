package com.example.tienda_bicicletas.dto;

import com.example.tienda_bicicletas.enums.Rol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDto {
    private Long id;
    private String username;
    private String nombre;
    private Rol rol;
    private long totalVentas;
}
