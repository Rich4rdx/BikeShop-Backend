package com.example.tienda_bicicletas.mapper;

import com.example.tienda_bicicletas.dto.DetalleVentaRequestDto;
import com.example.tienda_bicicletas.dto.DetalleVentaResponseDto;
import com.example.tienda_bicicletas.entity.Bicicleta;
import com.example.tienda_bicicletas.entity.DetalleVenta;
import com.example.tienda_bicicletas.entity.Venta;
import com.example.tienda_bicicletas.repository.BicicletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class DetalleVentaMapper {

    private final BicicletaRepository bicicletaRepository ;


    public DetalleVenta toEntity(DetalleVentaRequestDto dto, Venta venta) {
        Bicicleta bici = bicicletaRepository.findById(dto.getBicicletaId())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con ID " ));

        return DetalleVenta.builder()
                .venta(venta)
                .bicicleta(bici)
                .cantidad(dto.getCantidad())
                .precioUnitario(bici.getPrecio())
                .subtotal(bici.getPrecio().multiply(BigDecimal.valueOf(dto.getCantidad())))
                .build();
    }


    public DetalleVentaResponseDto toResponseDto(DetalleVenta entidad) {
        return DetalleVentaResponseDto.builder()
                .marcaBicicleta(entidad.getBicicleta().getMarca())
                .modeloBicicleta(entidad.getBicicleta().getModelo())
                .cantidad(entidad.getCantidad())
                .precioUnitario(entidad.getPrecioUnitario())
                .subtotal(entidad.getSubtotal())
                .build();
    }
}
