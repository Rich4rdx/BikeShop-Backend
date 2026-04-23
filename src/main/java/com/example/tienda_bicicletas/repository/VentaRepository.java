package com.example.tienda_bicicletas.repository;

import com.example.tienda_bicicletas.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Venta> findByClienteDocumento(String documento);
    List<Venta> findByUsuarioId(Long usuarioId);
    long countByUsuarioId(Long usuarioId);
}
