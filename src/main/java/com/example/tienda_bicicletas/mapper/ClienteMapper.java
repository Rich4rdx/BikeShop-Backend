package com.example.tienda_bicicletas.mapper;

import com.example.tienda_bicicletas.dto.ClienteRequestDto;
import com.example.tienda_bicicletas.dto.ClienteResponseDto;
import com.example.tienda_bicicletas.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class  ClienteMapper {

    public Cliente toEntity (ClienteRequestDto dto){
        return Cliente.builder()
                .documento(dto.getDocumento())
                .nombre(dto.getNombre())
                .telefono(dto.getTelefono())
                .build();
    }


    public ClienteResponseDto toResponseDto(Cliente entidad){
        return ClienteResponseDto.builder()
                .documento(entidad.getDocumento())
                .nombre(entidad.getNombre())
                .telefono(entidad.getTelefono())
                .build();
    }
}
