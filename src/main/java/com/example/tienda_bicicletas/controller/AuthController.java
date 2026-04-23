package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.*;
import com.example.tienda_bicicletas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(
            @Valid @RequestBody RegistroRequestDto dto) {
        authService.registrarEmpleado(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Empleado registrado exitosamente");
    }
}