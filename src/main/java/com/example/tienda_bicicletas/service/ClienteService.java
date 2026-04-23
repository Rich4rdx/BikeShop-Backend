package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.ClienteRequestDto;
import com.example.tienda_bicicletas.dto.ClienteResponseDto;
import com.example.tienda_bicicletas.entity.Cliente;
import com.example.tienda_bicicletas.mapper.ClienteMapper;
import com.example.tienda_bicicletas.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDto registrar(ClienteRequestDto dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        if (clienteRepository.existsById(dto.getDocumento())){

            throw new RuntimeException("ya existe un usuario regisgtrado ");
        }

        if (cliente.getDocumento() == null || cliente.getNombre() == null) {
            throw new RuntimeException("Los campos documento y nombre son obligatorios");
        }
        return clienteMapper.toResponseDto(clienteRepository.save(cliente));
    }

    public List<ClienteResponseDto> listar() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDto)
                .toList();
    }

    public ClienteResponseDto buscarPorDocumento(String documento) {
        Cliente cliente = clienteRepository.findById(documento)
                .orElseThrow(() -> new RuntimeException("El cliente no existe"));
        return clienteMapper.toResponseDto(cliente);
    }



}
