package com.example.tienda_bicicletas.repository;

import com.example.tienda_bicicletas.entity.HistorialCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialComprasRepository extends JpaRepository<HistorialCompras, Long> {
    List<HistorialCompras> findByProveedorNit(String nit);
    List<HistorialCompras> findByBicicletaCodigo(Long codigo);
}
