package com.example.tienda_bicicletas.entity;


import com.example.tienda_bicicletas.enums.TipoBicicleta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "bicicleta")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;

    @Column(nullable = false, length = 50)
    private String marca;


    @Column(nullable = false,length = 50)
    private String modelo;

    @Enumerated(EnumType.STRING)
    private TipoBicicleta tipo;

    @Column(nullable = false,precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer cantidadInventario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nit_proveedor", nullable = true)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "bicicleta")
    private List<DetalleVenta> detalles;




}
