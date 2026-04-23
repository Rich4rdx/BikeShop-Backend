package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.DetalleVentaRequestDto;
import com.example.tienda_bicicletas.dto.VentaRequestDto;
import com.example.tienda_bicicletas.dto.VentaResponseDto;
import com.example.tienda_bicicletas.entity.*;
import com.example.tienda_bicicletas.enums.TipoMovimiento;
import com.example.tienda_bicicletas.mapper.VentaMapper;
import com.example.tienda_bicicletas.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimientoRepository movimientoRepository;
    private final BicicletaRepository bicicletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaMapper ventaMapper;

    @Transactional
    public VentaResponseDto registrarVenta(VentaRequestDto request) {

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("la venta no puede estar vacia");
        }

        Cliente cliente = clienteRepository.findById(request.getDocumentoCliente())
                .orElseThrow(() -> new RuntimeException("no se encontro ningun cliente con ese documento"));

        // Obtener el usuario autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioActual = usuarioRepository.findByUsername(username).orElse(null);

        Venta venta = Venta.builder()
                .cliente(cliente)
                .usuario(usuarioActual)
                .fechaVenta(java.time.LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .detalles(new ArrayList<>())
                .build();

        BigDecimal totalVenta = BigDecimal.ZERO;

        for (DetalleVentaRequestDto item : request.getDetalles()) {
            Bicicleta bici = bicicletaRepository.findById(item.getBicicletaId())
                    .orElseThrow(() -> new RuntimeException("no se encontro ninguna bicicleta por ese id"));

            if (bici.getCantidadInventario() < item.getCantidad()) {
                throw new RuntimeException("Cantida insfuciente en el inventario para: " + bici.getModelo());
            }

            BigDecimal subTotal = bici.getPrecio().multiply(new BigDecimal(item.getCantidad()));
            totalVenta = totalVenta.add(subTotal);

            bici.setCantidadInventario(bici.getCantidadInventario() - item.getCantidad());
            bicicletaRepository.save(bici);

            Movimiento movimiento = Movimiento.builder()
                    .bicicleta(bici)
                    .cantidad(item.getCantidad())
                    .tipoMovimiento(TipoMovimiento.SALIDA)
                    .fecha(LocalDate.now())
                    .build();
            movimientoRepository.save(movimiento);

            DetalleVenta detalle = DetalleVenta.builder()
                    .cantidad(item.getCantidad())
                    .subtotal(subTotal)
                    .bicicleta(bici)
                    .venta(venta)
                    .precioUnitario(bici.getPrecio())
                    .build();

            venta.getDetalles().add(detalle);
        }

        venta.setTotal(totalVenta);
        Venta ventaGuardada = ventaRepository.save(venta);
        return ventaMapper.toResponseDto(ventaGuardada);
    }

    @Transactional
    public List<VentaResponseDto> listar() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public VentaResponseDto obtenerPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        return ventaMapper.toResponseDto(venta);
    }

    @Transactional
    public List<VentaResponseDto> listarPorCliente(String documento) {
        return ventaRepository.findByClienteDocumento(documento).stream()
                .map(ventaMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public List<VentaResponseDto> listarPorUsuario(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId).stream()
                .map(ventaMapper::toResponseDto)
                .toList();
    }
}
