package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.VentaRequestDto;
import com.example.tienda_bicicletas.dto.VentaResponseDto;
import com.example.tienda_bicicletas.service.VentaService;
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
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Operaciones para registrar ventas y facturación")

public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    @Operation(summary = "Registrar una nueva venta",
            description = "Crea la venta, actualiza el stock de bicicletas y genera registros en la tabla Movimientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta procesada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud "),
            @ApiResponse(responseCode = "404", description = "Cliente o Bicicleta no encontrados")
    })
    public ResponseEntity<VentaResponseDto> registrarVenta(@Valid @RequestBody VentaRequestDto dto) {

        VentaResponseDto factura = ventaService.registrarVenta(dto);
        return new ResponseEntity<>(factura, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar historial de ventas", description = "Retorna todas las facturas generadas")
    public ResponseEntity<List<VentaResponseDto>> listarVentas() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar venta por ID", description = "Obtiene el detalle de una factura específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "La venta no existe")
    })
    public ResponseEntity<VentaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{documento}")
    @Operation(summary = "Historial de compras por cliente")
    public ResponseEntity<List<VentaResponseDto>> listarPorCliente(@PathVariable String documento) {
        return ResponseEntity.ok(ventaService.listarPorCliente(documento));
    }

}
