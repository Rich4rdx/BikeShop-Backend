package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.RegistroRequestDto;
import com.example.tienda_bicicletas.dto.UsuarioResponseDto;
import com.example.tienda_bicicletas.dto.VentaResponseDto;
import com.example.tienda_bicicletas.service.AuthService;
import com.example.tienda_bicicletas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios con su total de ventas")
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario (solo ADMIN)")
    public ResponseEntity<String> crear(@Valid @RequestBody RegistroRequestDto dto) {
        authService.registrarEmpleado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
    }

    @GetMapping("/{id}/ventas")
    @Operation(summary = "Ventas realizadas por un usuario específico")
    public ResponseEntity<List<VentaResponseDto>> ventasPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.ventasPorUsuario(id));
    }
}
