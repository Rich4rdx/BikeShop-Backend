package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.ReporteVentasDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExcelReporteService {

    public byte[] generarExcel(ReporteVentasDto r) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {

            CellStyle headerStyle = crearEstiloHeader(wb);
            CellStyle titleStyle  = crearEstiloTitulo(wb);
            CellStyle moneyStyle  = crearEstiloDinero(wb);
            CellStyle subStyle    = crearEstiloSubtitulo(wb);

            crearHojaResumen(wb, r, titleStyle, headerStyle, moneyStyle, subStyle);
            crearHojaTopBicicletas(wb, r, titleStyle, headerStyle, moneyStyle);
            crearHojaVentas(wb, r, titleStyle, headerStyle, moneyStyle);
            crearHojaVentasPorDia(wb, r, titleStyle, headerStyle, moneyStyle);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    private void crearHojaResumen(Workbook wb, ReporteVentasDto r,
                                   CellStyle title, CellStyle header,
                                   CellStyle money, CellStyle sub) {
        Sheet sheet = wb.createSheet("Resumen");
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 6000);

        Row t = sheet.createRow(0);
        Cell tc = t.createCell(0);
        tc.setCellValue("BIKESHOP — REPORTE DE VENTAS");
        tc.setCellStyle(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        Row periodo = sheet.createRow(1);
        periodo.createCell(0).setCellValue("Período: " + r.getFechaInicio() + " al " + r.getFechaFin());
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));

        sheet.createRow(2);

        String[][] datos = {
            {"Total de Ventas", String.valueOf(r.getTotalVentas())},
            {"Ingresos Totales", formatCOP(r.getTotalIngresos().doubleValue())},
            {"Promedio por Venta", formatCOP(r.getPromedioPorVenta().doubleValue())},
            {"Cliente Más Frecuente", r.getClienteFrecuente()},
            {"Compras del Cliente", String.valueOf(r.getComprasClienteFrecuente())}
        };

        int rowNum = 3;
        for (String[] fila : datos) {
            Row row = sheet.createRow(rowNum++);
            Cell label = row.createCell(0);
            label.setCellValue(fila[0]);
            label.setCellStyle(sub);
            row.createCell(1).setCellValue(fila[1]);
        }
    }

    private void crearHojaTopBicicletas(Workbook wb, ReporteVentasDto r,
                                         CellStyle title, CellStyle header, CellStyle money) {
        Sheet sheet = wb.createSheet("Top Bicicletas");
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 5000);

        Row t = sheet.createRow(0);
        Cell tc = t.createCell(0);
        tc.setCellValue("TOP 5 BICICLETAS MÁS VENDIDAS");
        tc.setCellStyle(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        sheet.createRow(1);
        Row h = sheet.createRow(2);
        String[] cols = {"#", "Marca", "Modelo", "Unidades", "Ingresos"};
        for (int i = 0; i < cols.length; i++) {
            Cell c = h.createCell(i);
            c.setCellValue(cols[i]);
            c.setCellStyle(header);
        }

        int rowNum = 3;
        int pos = 1;
        for (var b : r.getTopBicicletas()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pos++);
            row.createCell(1).setCellValue(b.getMarca());
            row.createCell(2).setCellValue(b.getModelo());
            row.createCell(3).setCellValue(b.getCantidadVendida());
            Cell ing = row.createCell(4);
            ing.setCellValue(b.getIngresosTotales().doubleValue());
            ing.setCellStyle(money);
        }
    }

    private void crearHojaVentas(Workbook wb, ReporteVentasDto r,
                                  CellStyle title, CellStyle header, CellStyle money) {
        Sheet sheet = wb.createSheet("Ventas");
        sheet.setColumnWidth(0, 2500);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 4000);

        Row t = sheet.createRow(0);
        Cell tc = t.createCell(0);
        tc.setCellValue("LISTADO DE VENTAS");
        tc.setCellStyle(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        sheet.createRow(1);
        Row h = sheet.createRow(2);
        String[] cols = {"# Venta", "Cliente", "Documento", "Items", "Total", "Fecha"};
        for (int i = 0; i < cols.length; i++) {
            Cell c = h.createCell(i);
            c.setCellValue(cols[i]);
            c.setCellStyle(header);
        }

        int rowNum = 3;
        for (var v : r.getVentasRecientes()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("#" + v.getIdVenta());
            row.createCell(1).setCellValue(v.getCliente());
            row.createCell(2).setCellValue(v.getDocumentoCliente());
            row.createCell(3).setCellValue(v.getDetalles().size());
            Cell total = row.createCell(4);
            total.setCellValue(v.getTotal().doubleValue());
            total.setCellStyle(money);
            row.createCell(5).setCellValue(v.getFecha() != null ? v.getFecha().toString() : "");
        }
    }

    private void crearHojaVentasPorDia(Workbook wb, ReporteVentasDto r,
                                        CellStyle title, CellStyle header, CellStyle money) {
        Sheet sheet = wb.createSheet("Ventas por Día");
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 3500);
        sheet.setColumnWidth(2, 5000);

        Row t = sheet.createRow(0);
        Cell tc = t.createCell(0);
        tc.setCellValue("VENTAS POR DÍA");
        tc.setCellStyle(title);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        sheet.createRow(1);
        Row h = sheet.createRow(2);
        String[] cols = {"Fecha", "Nº Ventas", "Total del Día"};
        for (int i = 0; i < cols.length; i++) {
            Cell c = h.createCell(i);
            c.setCellValue(cols[i]);
            c.setCellStyle(header);
        }

        int rowNum = 3;
        for (var d : r.getVentasPorDia()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(d.getFecha().toString());
            row.createCell(1).setCellValue(d.getCantidadVentas());
            Cell total = row.createCell(2);
            total.setCellValue(d.getTotalDia().doubleValue());
            total.setCellStyle(money);
        }
    }

    // ─── Estilos ───────────────────────────────────────────────
    private CellStyle crearEstiloTitulo(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 14);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }

    private CellStyle crearEstiloHeader(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }

    private CellStyle crearEstiloSubtitulo(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        s.setFont(f);
        return s;
    }

    private CellStyle crearEstiloDinero(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        DataFormat fmt = wb.createDataFormat();
        s.setDataFormat(fmt.getFormat("$#,##0"));
        return s;
    }

    private String formatCOP(double value) {
        return "$" + String.format("%,.0f", value);
    }
}
