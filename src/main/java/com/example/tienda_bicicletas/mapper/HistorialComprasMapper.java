package com.example.tienda_bicicletas.mapper;

import com.example.tienda_bicicletas.dto.HistorialComprasRequestDto;
import com.example.tienda_bicicletas.dto.HistorialComprasResponseDto;
import com.example.tienda_bicicletas.entity.HistorialCompras;
import org.springframework.stereotype.Component;

@Component
public class HistorialComprasMapper {

    public HistorialComprasResponseDto toResponseDto(HistorialCompras entidad) {
        return HistorialComprasResponseDto.builder()
                .id(entidad.getId())
                .nitProveedor(entidad.getProveedor().getNit())
                .nombreProveedor(entidad.getProveedor().getNombre())
                .codigoBicicleta(entidad.getBicicleta().getCodigo())
                .marcaBicicleta(entidad.getBicicleta().getMarca())
                .modeloBicicleta(entidad.getBicicleta().getModelo())
                .cantidad(entidad.getCantidad())
                .precioUnitario(entidad.getPrecioUnitario())
                .totalCompra(entidad.getTotalCompra())
                .fechaCompra(entidad.getFechaCompra())
                .notas(entidad.getNotas())
                .build();
    }
}