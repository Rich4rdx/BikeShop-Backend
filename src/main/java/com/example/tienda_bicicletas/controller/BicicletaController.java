package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.BicicletaRequestDto;
import com.example.tienda_bicicletas.dto.BicicletaResponseDto;
import com.example.tienda_bicicletas.service.BicicletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicicletas")
@RequiredArgsConstructor
@Tag(name = "Bicicletas", description = "Operaciones para la gestión de inventario de bicicletas")
public class BicicletaController {
    private final BicicletaService bicicletaService;

    @PostMapping
    @Operation(summary = "Registrar una nueva bicicleta", description = "Crea una bicicleta asociada a un proveedor y valida el stock inicial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bicicleta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Error de validación Bean)"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BicicletaResponseDto> crear(@Valid @RequestBody BicicletaRequestDto dto) {
        return new ResponseEntity<>(bicicletaService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas las bicicletas", description = "Retorna el catálogo completo disponible con información de proveedores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay bicicletas registradas")
    })
    public ResponseEntity<List<BicicletaResponseDto>> listarBicicletas() {
        List<BicicletaResponseDto> lista = bicicletaService.listar();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bicicleta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bicicleta encontrada"),
            @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada con el ID proporcionado")
    })
    public ResponseEntity<BicicletaResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bicicletaService.obtenerPorId(id));
    }

    @GetMapping("/proveedor/{nit}")
    @Operation(summary = "Obtener bicicletas por proveedor", description = "Retorna todas las bicicletas que surte un proveedor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bicicletas obtenidas correctamente"),
            @ApiResponse(responseCode = "204", description = "El proveedor no tiene bicicletas registradas"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<List<BicicletaResponseDto>> obtenerPorProveedor(@PathVariable String nit) {
        List<BicicletaResponseDto> lista = bicicletaService.obtenerBicicletasPorProveedor(nit);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

}
