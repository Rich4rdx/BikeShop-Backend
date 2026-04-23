package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.*;
import com.example.tienda_bicicletas.entity.DetalleVenta;
import com.example.tienda_bicicletas.entity.Venta;
import com.example.tienda_bicicletas.mapper.VentaMapper;
import com.example.tienda_bicicletas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    @Transactional(readOnly = true)
    public ReporteVentasDto generarReporte(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin    = fechaFin.atTime(23, 59, 59);

        List<Venta> ventas = ventaRepository.findByFechaVentaBetween(inicio, fin);

        int totalVentas = ventas.size();

        BigDecimal totalIngresos = ventas.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal promedio = totalVentas > 0
                ? totalIngresos.divide(BigDecimal.valueOf(totalVentas), 0, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Cliente más frecuente
        Map<String, Long> clienteConteo = ventas.stream()
                .filter(v -> v.getCliente() != null)
                .collect(Collectors.groupingBy(
                        v -> v.getCliente().getNombre(), Collectors.counting()
                ));
        String clienteFrecuente = "—";
        int comprasClienteFrecuente = 0;
        if (!clienteConteo.isEmpty()) {
            Map.Entry<String, Long> top = Collections.max(
                    clienteConteo.entrySet(), Map.Entry.comparingByValue()
            );
            clienteFrecuente = top.getKey();
            comprasClienteFrecuente = top.getValue().intValue();
        }


        List<TopBicicletaDto> topBicicletas = calcularTopBicicletas(ventas);


        List<VentaPorDiaDto> ventasPorDia = calcularVentasPorDia(ventas);


        List<VentaResponseDto> ventasRecientes = ventas.stream()
                .sorted(Comparator.comparing(Venta::getFechaVenta).reversed())
                .limit(10)
                .map(ventaMapper::toResponseDto)
                .collect(Collectors.toList());

        return ReporteVentasDto.builder()
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .totalVentas(totalVentas)
                .totalIngresos(totalIngresos)
                .promedioPorVenta(promedio)
                .clienteFrecuente(clienteFrecuente)
                .comprasClienteFrecuente(comprasClienteFrecuente)
                .topBicicletas(topBicicletas)
                .ventasPorDia(ventasPorDia)
                .ventasRecientes(ventasRecientes)
                .build();
    }

    private List<TopBicicletaDto> calcularTopBicicletas(List<Venta> ventas) {
        Map<String, TopBicicletaDto> mapa = new LinkedHashMap<>();
        for (Venta venta : ventas) {
            for (DetalleVenta d : venta.getDetalles()) {
                String key = d.getBicicleta().getMarca() + "|" + d.getBicicleta().getModelo();
                if (mapa.containsKey(key)) {
                    TopBicicletaDto e = mapa.get(key);
                    mapa.put(key, new TopBicicletaDto(
                            e.getMarca(), e.getModelo(),
                            e.getCantidadVendida() + d.getCantidad(),
                            e.getIngresosTotales().add(d.getSubtotal())
                    ));
                } else {
                    mapa.put(key, new TopBicicletaDto(
                            d.getBicicleta().getMarca(),
                            d.getBicicleta().getModelo(),
                            d.getCantidad(),
                            d.getSubtotal()
                    ));
                }
            }
        }
        return mapa.values().stream()
                .sorted(Comparator.comparingInt(TopBicicletaDto::getCantidadVendida).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<VentaPorDiaDto> calcularVentasPorDia(List<Venta> ventas) {
        Map<LocalDate, List<Venta>> porDia = ventas.stream()
                .collect(Collectors.groupingBy(v -> v.getFechaVenta().toLocalDate()));

        return porDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new VentaPorDiaDto(
                        e.getKey(),
                        e.getValue().size(),
                        e.getValue().stream().map(Venta::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .collect(Collectors.toList());
    }
}
