package com.example.tienda_bicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDto {

    @NotBlank(message = "el campo del documento es obligatorio")
    @Size(max = 20, message = "el campo de documento no puede tener mas de 20 caracteres")
    private String documento;

    @NotBlank(message = "el campo del nombre es obligatorio")
    @Size(max = 100 )
    private String nombre;

    @NotBlank(message = "el campo de telefono es obligatorio")
    @Size(max = 20)
    private String telefono;

}
