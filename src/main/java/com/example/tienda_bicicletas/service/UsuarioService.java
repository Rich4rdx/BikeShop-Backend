package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.UsuarioResponseDto;
import com.example.tienda_bicicletas.dto.VentaResponseDto;
import com.example.tienda_bicicletas.mapper.VentaMapper;
import com.example.tienda_bicicletas.repository.UsuarioRepository;
import com.example.tienda_bicicletas.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    @Transactional
    public List<UsuarioResponseDto> listar() {
        return usuarioRepository.findAll().stream()
                .map(u -> UsuarioResponseDto.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .nombre(u.getNombre())
                        .rol(u.getRol())
                        .totalVentas(ventaRepository.countByUsuarioId(u.getId()))
                        .build())
                .toList();
    }

    @Transactional
    public List<VentaResponseDto> ventasPorUsuario(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId).stream()
                .map(ventaMapper::toResponseDto)
                .toList();
    }
}
