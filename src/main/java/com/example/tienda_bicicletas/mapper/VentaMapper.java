package com.example.tienda_bicicletas.mapper;

import com.example.tienda_bicicletas.dto.VentaResponseDto;
import com.example.tienda_bicicletas.entity.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VentaMapper {

    private final DetalleVentaMapper detalleVentaMapper;

    public VentaResponseDto toResponseDto(Venta venta) {
        return VentaResponseDto.builder()
                .idVenta(venta.getId())
                .fecha(venta.getFechaVenta())
                .cliente(venta.getCliente() != null ? venta.getCliente().getNombre() : "Cliente no registrado")
                .documentoCliente(venta.getCliente() != null ? venta.getCliente().getDocumento() : "")
                .total(venta.getTotal())
                .usuarioNombre(venta.getUsuario() != null ? venta.getUsuario().getNombre() : "")
                .detalles(venta.getDetalles().stream()
                        .map(detalleVentaMapper::toResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
