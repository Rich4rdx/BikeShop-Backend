package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.MovimientoRequestDto;
import com.example.tienda_bicicletas.dto.MovimientoResponseDto;
import com.example.tienda_bicicletas.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDto>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    @PostMapping
    public ResponseEntity<MovimientoResponseDto> registrar(
            @Valid @RequestBody MovimientoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrar(dto));
    }
}
