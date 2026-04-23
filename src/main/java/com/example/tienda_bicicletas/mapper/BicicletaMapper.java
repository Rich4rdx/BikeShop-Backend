package com.example.tienda_bicicletas.mapper;

import com.example.tienda_bicicletas.dto.BicicletaRequestDto;
import com.example.tienda_bicicletas.dto.BicicletaResponseDto;
import com.example.tienda_bicicletas.entity.Bicicleta;
import com.example.tienda_bicicletas.entity.Proveedor;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;

@Component
public class BicicletaMapper {

    public Bicicleta toEntity(BicicletaRequestDto dto,Proveedor proveedor){
        return Bicicleta.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo() )
                .tipo(dto.getTipo())
                .precio(dto.getPrecio())
                .cantidadInventario(dto.getCantidadInventario())
                .proveedor(proveedor)
                .build();
    }

    public BicicletaResponseDto toResponseDto(Bicicleta entidad){
        return BicicletaResponseDto.builder()
                .codigo(entidad.getCodigo())
                .marca(entidad.getMarca())
                .modelo(entidad.getModelo())
                .tipo(entidad.getTipo())
                .precio(entidad.getPrecio())
                .cantidadInventario(entidad.getCantidadInventario())
                .nitProveedor(entidad.getProveedor().getNit())
                .nombreProveedor(entidad.getProveedor().getNombre())
                .build();
    }

}
