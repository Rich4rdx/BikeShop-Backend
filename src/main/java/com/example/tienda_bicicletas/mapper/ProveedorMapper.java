package com.example.tienda_bicicletas.mapper;

import com.example.tienda_bicicletas.dto.ProveedorRequestDto;
import com.example.tienda_bicicletas.dto.ProveedorResponseDto;
import com.example.tienda_bicicletas.entity.Proveedor;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {

    public Proveedor toEntity(ProveedorRequestDto dto) {
        return Proveedor.builder()
                .nit(dto.getNit())
                .nombre(dto.getNombre())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .build();
    }

    public ProveedorResponseDto toResponseDto(Proveedor entidad) {
        return ProveedorResponseDto.builder()
                .nit(entidad.getNit())
                .nombre(entidad.getNombre())
                .telefono(entidad.getTelefono())
                .email(entidad.getEmail())
                .direccion(entidad.getDireccion())
                .ciudad(entidad.getCiudad())
                .build();
    }
}