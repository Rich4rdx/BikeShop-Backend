package com.example.tienda_bicicletas.dto;

import com.example.tienda_bicicletas.enums.Rol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String token;
    private String nombre;
    private String username;
    private Rol rol;
}
