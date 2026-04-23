package com.example.tienda_bicicletas.repository;

import com.example.tienda_bicicletas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository  extends JpaRepository<Movimiento,Long> {
    List<Movimiento> findAllByOrderByFechaDesc();
}
