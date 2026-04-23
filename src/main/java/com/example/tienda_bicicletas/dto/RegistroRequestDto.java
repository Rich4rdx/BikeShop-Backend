package com.example.tienda_bicicletas.dto;

import com.example.tienda_bicicletas.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroRequestDto {
    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String password;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    private Rol rol = Rol.TRABAJADOR;
}
