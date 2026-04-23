package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.BicicletaRequestDto;
import com.example.tienda_bicicletas.dto.BicicletaResponseDto;
import com.example.tienda_bicicletas.entity.Bicicleta;
import com.example.tienda_bicicletas.entity.Movimiento;
import com.example.tienda_bicicletas.entity.Proveedor;
import com.example.tienda_bicicletas.enums.TipoMovimiento;
import com.example.tienda_bicicletas.mapper.BicicletaMapper;
import com.example.tienda_bicicletas.repository.BicicletaRepository;
import com.example.tienda_bicicletas.repository.MovimientoRepository;
import com.example.tienda_bicicletas.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final BicicletaMapper bicicletaMapper;
    private final MovimientoRepository movimientoRepository;
    private final ProveedorRepository proveedorRepository;

    @Transactional
    public BicicletaResponseDto crear(BicicletaRequestDto dto) {

        Proveedor proveedor = proveedorRepository.findByNit(dto.getNitProveedor())
                .orElseThrow(() -> new RuntimeException("El proveedor con NIT " + dto.getNitProveedor() + " no existe en la base de datos"));


        Bicicleta bicicleta = bicicletaMapper.toEntity(dto, proveedor);


        if (bicicleta.getModelo() == null || bicicleta.getMarca() == null) {
            throw new RuntimeException("Los campos marca y modelo son obligatorios");
        }


        Bicicleta guardada = bicicletaRepository.save(bicicleta);


        Movimiento movimiento = Movimiento.builder()
                .bicicleta(guardada)
                .cantidad(dto.getCantidadInventario())
                .tipoMovimiento(TipoMovimiento.ENTRADA)
                .fecha(LocalDate.now())
                .build();
        movimientoRepository.save(movimiento);

        return bicicletaMapper.toResponseDto(guardada);
    }

    @Transactional(readOnly = true)
    public List<BicicletaResponseDto> listar() {
        // Obtenemos todas las bicicletas y las convertimos a DTO
        // El Mapper se encargará de manejar si un proveedor "desapareció" de la DB
        return bicicletaRepository.findAll().stream()
                .map(bicicletaMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BicicletaResponseDto obtenerPorId(Long id) {
        Bicicleta bici = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La bicicleta con ID " + id + " no existe"));

        return bicicletaMapper.toResponseDto(bici);
    }

    @Transactional
    public BicicletaResponseDto actualizarInventario(Long id, int nuevaCantidad) {
        Bicicleta bici = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la bicicleta para actualizar"));

        // Podrías agregar lógica aquí para registrar un movimiento de AJUSTE si la cantidad cambia
        bici.setCantidadInventario(nuevaCantidad);
        Bicicleta actualizada = bicicletaRepository.save(bici);

        return bicicletaMapper.toResponseDto(actualizada);
    }

    @Transactional(readOnly = true)
    public List<BicicletaResponseDto> obtenerBicicletasPorProveedor(String nitProveedor) {
        if (!proveedorRepository.existsByNit(nitProveedor)) {
            throw new RuntimeException("No se pueden listar bicicletas: El proveedor no existe");
        }

        return bicicletaRepository.findByProveedorNit(nitProveedor).stream()
                .map(bicicletaMapper::toResponseDto)
                .toList();
    }
}