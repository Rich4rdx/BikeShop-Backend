package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.MovimientoRequestDto;
import com.example.tienda_bicicletas.dto.MovimientoResponseDto;
import com.example.tienda_bicicletas.entity.Bicicleta;
import com.example.tienda_bicicletas.entity.Movimiento;
import com.example.tienda_bicicletas.enums.TipoMovimiento;
import com.example.tienda_bicicletas.repository.BicicletaRepository;
import com.example.tienda_bicicletas.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final BicicletaRepository bicicletaRepository;

    public List<MovimientoResponseDto> listar() {
        return movimientoRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovimientoResponseDto registrar(MovimientoRequestDto dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getBicicletaId())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

        if (dto.getTipo() == TipoMovimiento.SALIDA) {
            if (bicicleta.getCantidadInventario() < dto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + bicicleta.getModelo());
            }
            bicicleta.setCantidadInventario(bicicleta.getCantidadInventario() - dto.getCantidad());
        } else {
            bicicleta.setCantidadInventario(bicicleta.getCantidadInventario() + dto.getCantidad());
        }
        bicicletaRepository.save(bicicleta);

        Movimiento movimiento = Movimiento.builder()
                .tipoMovimiento(dto.getTipo())
                .cantidad(dto.getCantidad())
                .fecha(LocalDate.now())
                .bicicleta(bicicleta)
                .build();

        movimientoRepository.save(movimiento);
        return toDto(movimiento);
    }

    private MovimientoResponseDto toDto(Movimiento m) {
        return MovimientoResponseDto.builder()
                .id(m.getIdMovimiento())
                .tipo(m.getTipoMovimiento())
                .cantidad(m.getCantidad())
                .fecha(m.getFecha())
                .codigoBicicleta(m.getBicicleta().getCodigo())
                .marcaBicicleta(m.getBicicleta().getMarca())
                .modeloBicicleta(m.getBicicleta().getModelo())
                .build();
    }
}
