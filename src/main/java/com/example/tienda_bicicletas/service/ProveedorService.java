package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.*;
import com.example.tienda_bicicletas.entity.*;
import com.example.tienda_bicicletas.mapper.HistorialComprasMapper;
import com.example.tienda_bicicletas.mapper.ProveedorMapper;
import com.example.tienda_bicicletas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final HistorialComprasRepository historialComprasRepository;
    private final BicicletaRepository bicicletaRepository;
    private final ProveedorMapper proveedorMapper;
    private final HistorialComprasMapper historialComprasMapper;



    public ProveedorResponseDto crear(ProveedorRequestDto dto) {
        if (proveedorRepository.findByNit(dto.getNit()).isPresent()) {
            throw new IllegalArgumentException("El proveedor con NIT " + dto.getNit() + " ya existe");
        }

        Proveedor proveedor = proveedorMapper.toEntity(dto);
        Proveedor guardado = proveedorRepository.save(proveedor);
        return proveedorMapper.toResponseDto(guardado);
    }

    public ProveedorResponseDto obtenerPorNit(String nit) {
        Proveedor proveedor = proveedorRepository.findByNit(nit)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con NIT: " + nit));
        return proveedorMapper.toResponseDto(proveedor);
    }

    public List<ProveedorResponseDto> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(proveedorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ProveedorResponseDto actualizar(String nit, ProveedorRequestDto dto) {
        Proveedor proveedor = proveedorRepository.findByNit(nit)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con NIT: " + nit));

        proveedor.setNombre(dto.getNombre());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setCiudad(dto.getCiudad());

        Proveedor actualizado = proveedorRepository.save(proveedor);
        return proveedorMapper.toResponseDto(actualizado);
    }

    public void eliminar(String nit) {
        if (!proveedorRepository.existsById(nit)) {
            throw new IllegalArgumentException("Proveedor no encontrado con NIT: " + nit);
        }
        proveedorRepository.deleteById(nit);
    }



    public HistorialComprasResponseDto registrarCompra(HistorialComprasRequestDto dto) {
        Proveedor proveedor = proveedorRepository.findByNit(dto.getNitProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Bicicleta bicicleta = bicicletaRepository.findById(dto.getCodigoBicicleta())
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta no encontrada"));


        java.math.BigDecimal totalCompra = dto.getPrecioUnitario()
                .multiply(java.math.BigDecimal.valueOf(dto.getCantidad()));

        HistorialCompras compra = HistorialCompras.builder()
                .proveedor(proveedor)
                .bicicleta(bicicleta)
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .totalCompra(totalCompra)
                .fechaCompra(LocalDateTime.now())
                .notas(dto.getNotas())
                .build();


        bicicleta.setCantidadInventario(bicicleta.getCantidadInventario() + dto.getCantidad());
        bicicletaRepository.save(bicicleta);

        HistorialCompras guardada = historialComprasRepository.save(compra);
        return historialComprasMapper.toResponseDto(guardada);
    }

    public List<HistorialComprasResponseDto> obtenerComprasPorProveedor(String nit) {
        Proveedor proveedor = proveedorRepository.findByNit(nit)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        return historialComprasRepository.findByProveedorNit(nit).stream()
                .map(historialComprasMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<HistorialComprasResponseDto> obtenerComprasPorBicicleta(Long codigoBicicleta) {
        return historialComprasRepository.findByBicicletaCodigo(codigoBicicleta).stream()
                .map(historialComprasMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<HistorialComprasResponseDto> obtenerTodoHistorial() {
        return historialComprasRepository.findAll().stream()
                .map(historialComprasMapper::toResponseDto)
                .collect(Collectors.toList());
    }}