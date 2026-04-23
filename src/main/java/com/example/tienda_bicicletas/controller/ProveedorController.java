package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.*;
import com.example.tienda_bicicletas.service.ProveedorService;
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
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "Operaciones para la gestión de proveedores de bicicletas")
public class ProveedorController {

    private final ProveedorService proveedorService;

    // ========== CRUD PROVEEDORES ==========

    @PostMapping
    @Operation(summary = "Registrar un nuevo proveedor", description = "Crea un nuevo proveedor en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProveedorResponseDto> crear(@Valid @RequestBody ProveedorRequestDto dto) {
        return new ResponseEntity<>(proveedorService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los proveedores", description = "Obtiene la lista completa de proveedores registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay proveedores registrados")
    })
    public ResponseEntity<List<ProveedorResponseDto>> listarTodos() {
        List<ProveedorResponseDto> lista = proveedorService.listarTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{nit}")
    @Operation(summary = "Obtener proveedor por NIT", description = "Retorna los datos de un proveedor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<ProveedorResponseDto> obtenerPorNit(@PathVariable String nit) {
        return ResponseEntity.ok(proveedorService.obtenerPorNit(nit));
    }

    @PutMapping("/{nit}")
    @Operation(summary = "Actualizar proveedor", description = "Actualiza los datos de un proveedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<ProveedorResponseDto> actualizar(@PathVariable String nit,
                                                           @Valid @RequestBody ProveedorRequestDto dto) {
        return ResponseEntity.ok(proveedorService.actualizar(nit, dto));
    }

    @DeleteMapping("/{nit}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable String nit) {
        proveedorService.eliminar(nit);
        return ResponseEntity.noContent().build();
    }

    // ========== HISTORIAL DE COMPRAS ==========

    @PostMapping("/compras/registrar")
    @Operation(summary = "Registrar una compra a un proveedor", description = "Crea un registro de compra y actualiza inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Proveedor o bicicleta no encontrado")
    })
    public ResponseEntity<HistorialComprasResponseDto> registrarCompra(@Valid @RequestBody HistorialComprasRequestDto dto) {
        return new ResponseEntity<>(proveedorService.registrarCompra(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{nit}/compras")
    @Operation(summary = "Obtener historial de compras de un proveedor", description = "Retorna todas las compras realizadas a un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<List<HistorialComprasResponseDto>> obtenerComprasPorProveedor(@PathVariable String nit) {
        List<HistorialComprasResponseDto> compras = proveedorService.obtenerComprasPorProveedor(nit);
        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/bicicleta/{codigoBicicleta}")
    @Operation(summary = "Obtener historial de compras por bicicleta", description = "Retorna todas las compras de una bicicleta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay compras registradas para esta bicicleta")
    })
    public ResponseEntity<List<HistorialComprasResponseDto>> obtenerComprasPorBicicleta(@PathVariable Long codigoBicicleta) {
        List<HistorialComprasResponseDto> compras = proveedorService.obtenerComprasPorBicicleta(codigoBicicleta);
        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/compras/todas")
    @Operation(summary = "Obtener todo el historial de compras", description = "Retorna el historial completo de compras de todos los proveedores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay compras registradas")
    })
    public ResponseEntity<List<HistorialComprasResponseDto>> obtenerTodoHistorial() {
        List<HistorialComprasResponseDto> compras = proveedorService.obtenerTodoHistorial();
        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(compras);
    }}