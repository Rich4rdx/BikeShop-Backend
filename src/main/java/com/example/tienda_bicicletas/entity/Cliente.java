package com.example.tienda_bicicletas.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cliente")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente {

    @Id
    @Column(nullable = false,unique = true,length = 20)
    private String documento;


    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(nullable = false,length = 20)
    private String telefono;
}
