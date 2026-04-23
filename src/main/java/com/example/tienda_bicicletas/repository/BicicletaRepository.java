package com.example.tienda_bicicletas.repository;

import com.example.tienda_bicicletas.entity.Bicicleta;
import com.example.tienda_bicicletas.enums.TipoBicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

    // Nota: Cambiamos a Mayúscula después del 'By' para cumplir la convención
    List<Bicicleta> findByMarca(String marca);

    List<Bicicleta> findByModelo(String modelo);

    List<Bicicleta> findByTipo(TipoBicicleta tipoBicicleta);

    List<Bicicleta> findByCantidadInventario(Integer cantidadInventario);

    List<Bicicleta> findByProveedorNit(String nit);

    // Tip Pro: Si quieres buscar una marca aunque el usuario escriba en minúsculas
    List<Bicicleta> findByMarcaContainingIgnoreCase(String marca);
}