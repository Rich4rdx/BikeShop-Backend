package com.example.tienda_bicicletas.repository;

import com.example.tienda_bicicletas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,String> {
}
