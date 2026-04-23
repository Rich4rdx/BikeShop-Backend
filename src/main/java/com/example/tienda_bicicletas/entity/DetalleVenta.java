package com.example.tienda_bicicletas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "detalle_venta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false )
    private Integer cantidad;

    @Column(nullable = false,precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "venta_id",nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "bicicleta_id", nullable = false )
    private Bicicleta bicicleta;
}
