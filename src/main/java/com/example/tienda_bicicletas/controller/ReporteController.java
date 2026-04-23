package com.example.tienda_bicicletas.controller;

import com.example.tienda_bicicletas.dto.ReporteVentasDto;
import com.example.tienda_bicicletas.service.ExcelReporteService;
import com.example.tienda_bicicletas.service.PdfReporteService;
import com.example.tienda_bicicletas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    private final ExcelReporteService excelService;
    private final PdfReporteService pdfService;

    @GetMapping("/ventas")
    public ResponseEntity<ReporteVentasDto> reporteJson(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.generarReporte(fechaInicio, fechaFin));
    }

    @GetMapping("/ventas/excel")
    public ResponseEntity<byte[]> reporteExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) throws Exception {
        ReporteVentasDto reporte = reporteService.generarReporte(fechaInicio, fechaFin);
        byte[] bytes = excelService.generarExcel(reporte);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"reporte-ventas-" + fechaInicio + "-" + fechaFin + ".xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    @GetMapping("/ventas/pdf")
    public ResponseEntity<byte[]> reportePdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) throws Exception {
        ReporteVentasDto reporte = reporteService.generarReporte(fechaInicio, fechaFin);
        byte[] bytes = pdfService.generarPdf(reporte);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"reporte-ventas-" + fechaInicio + "-" + fechaFin + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}
