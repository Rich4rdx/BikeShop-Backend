    package com.example.tienda_bicicletas.dto;


    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotNull;
    import lombok.Data;

    @Data
    public class DetalleVentaRequestDto {

        @NotNull(message = "El código de la bicicleta es obligatorio")
        private Long bicicletaId;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima para vender es 1 unidad")
        private Integer cantidad;
    }
