package com.example.tienda_bicicletas.repository;

import com.example.tienda_bicicletas.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, String> {
    Optional<Proveedor> findByNit(String nit);
    Optional<Proveedor> findByEmail(String email);
    boolean existsByNit(String nit);
}
