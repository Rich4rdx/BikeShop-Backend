package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.ReporteVentasDto;
import com.example.tienda_bicicletas.dto.TopBicicletaDto;
import com.example.tienda_bicicletas.dto.VentaPorDiaDto;
import com.example.tienda_bicicletas.dto.VentaResponseDto;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfReporteService {

    private static final Color COLOR_PRIMARY   = new Color(0, 105, 92);
    private static final Color COLOR_HEADER    = new Color(230, 81, 0);
    private static final Color COLOR_ROW_ALT   = new Color(245, 245, 245);
    private static final Color COLOR_WHITE     = Color.WHITE;

    public byte[] generarPdf(ReporteVentasDto r) throws IOException, DocumentException {
        Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();


        Font fuenteTitulo   = new Font(Font.HELVETICA, 18, Font.BOLD, COLOR_WHITE);
        Font fuenteSubtitulo= new Font(Font.HELVETICA, 10, Font.NORMAL, COLOR_WHITE);
        Font fuenteSeccion  = new Font(Font.HELVETICA, 12, Font.BOLD, COLOR_PRIMARY);
        Font fuenteHeaderTbl= new Font(Font.HELVETICA, 9,  Font.BOLD, COLOR_WHITE);
        Font fuenteNormal   = new Font(Font.HELVETICA, 9,  Font.NORMAL, Color.DARK_GRAY);
        Font fuenteKpiVal   = new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_PRIMARY);
        Font fuenteKpiLabel = new Font(Font.HELVETICA, 8,  Font.NORMAL, Color.GRAY);


        PdfPTable headerTbl = new PdfPTable(1);
        headerTbl.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(COLOR_PRIMARY);
        headerCell.setPadding(16);
        headerCell.setBorder(Rectangle.NO_BORDER);
        Paragraph titulo = new Paragraph("BikeShop", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_LEFT);
        headerCell.addElement(titulo);
        Paragraph sub = new Paragraph(
                "Reporte de Ventas  |  Período: " + r.getFechaInicio() + " — " + r.getFechaFin(),
                fuenteSubtitulo);
        headerCell.addElement(sub);
        headerTbl.addCell(headerCell);
        doc.add(headerTbl);
        doc.add(Chunk.NEWLINE);


        PdfPTable kpiTbl = new PdfPTable(4);
        kpiTbl.setWidthPercentage(100);
        kpiTbl.setSpacingAfter(16);

        agregarKpi(kpiTbl, "Total Ventas",      String.valueOf(r.getTotalVentas()),         fuenteKpiVal, fuenteKpiLabel);
        agregarKpi(kpiTbl, "Ingresos Totales",  formatCOP(r.getTotalIngresos().doubleValue()),  fuenteKpiVal, fuenteKpiLabel);
        agregarKpi(kpiTbl, "Ticket Promedio",   formatCOP(r.getPromedioPorVenta().doubleValue()), fuenteKpiVal, fuenteKpiLabel);
        agregarKpi(kpiTbl, "Cliente Frecuente", r.getClienteFrecuente() + " (" + r.getComprasClienteFrecuente() + " compras)", fuenteKpiVal, fuenteKpiLabel);
        doc.add(kpiTbl);


        doc.add(new Paragraph("Top 5 Bicicletas Más Vendidas", fuenteSeccion));
        doc.add(Chunk.NEWLINE);

        PdfPTable topTbl = new PdfPTable(new float[]{1, 3, 3, 2, 3});
        topTbl.setWidthPercentage(100);
        topTbl.setSpacingAfter(16);

        for (String col : new String[]{"#", "Marca", "Modelo", "Unidades", "Ingresos"}) {
            PdfPCell c = new PdfPCell(new Phrase(col, fuenteHeaderTbl));
            c.setBackgroundColor(COLOR_HEADER);
            c.setPadding(6);
            c.setBorder(Rectangle.NO_BORDER);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            topTbl.addCell(c);
        }

        int pos = 1;
        for (TopBicicletaDto b : r.getTopBicicletas()) {
            boolean alt = pos % 2 == 0;
            agregarFilaTop(topTbl, String.valueOf(pos++), b.getMarca(), b.getModelo(),
                    String.valueOf(b.getCantidadVendida()),
                    formatCOP(b.getIngresosTotales().doubleValue()),
                    alt, fuenteNormal);
        }
        doc.add(topTbl);


        if (!r.getVentasPorDia().isEmpty()) {
            doc.add(new Paragraph("Ventas por Día", fuenteSeccion));
            doc.add(Chunk.NEWLINE);

            PdfPTable diaTbl = new PdfPTable(new float[]{3, 2, 3});
            diaTbl.setWidthPercentage(100);
            diaTbl.setSpacingAfter(16);

            for (String col : new String[]{"Fecha", "Nº Ventas", "Total del Día"}) {
                PdfPCell c = new PdfPCell(new Phrase(col, fuenteHeaderTbl));
                c.setBackgroundColor(COLOR_HEADER);
                c.setPadding(6);
                c.setBorder(Rectangle.NO_BORDER);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                diaTbl.addCell(c);
            }

            int row = 1;
            for (VentaPorDiaDto d : r.getVentasPorDia()) {
                boolean alt = row++ % 2 == 0;
                Color bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
                for (String val : new String[]{
                        d.getFecha().toString(),
                        String.valueOf(d.getCantidadVentas()),
                        formatCOP(d.getTotalDia().doubleValue())}) {
                    PdfPCell c = new PdfPCell(new Phrase(val, fuenteNormal));
                    c.setBackgroundColor(bg);
                    c.setPadding(5);
                    c.setBorder(Rectangle.NO_BORDER);
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    diaTbl.addCell(c);
                }
            }
            doc.add(diaTbl);
        }


        doc.add(new Paragraph("Últimas Ventas del Período", fuenteSeccion));
        doc.add(Chunk.NEWLINE);

        PdfPTable ventasTbl = new PdfPTable(new float[]{1.5f, 3, 2, 1.5f, 2.5f, 2.5f});
        ventasTbl.setWidthPercentage(100);

        for (String col : new String[]{"# Venta", "Cliente", "Documento", "Items", "Total", "Fecha"}) {
            PdfPCell c = new PdfPCell(new Phrase(col, fuenteHeaderTbl));
            c.setBackgroundColor(COLOR_HEADER);
            c.setPadding(6);
            c.setBorder(Rectangle.NO_BORDER);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            ventasTbl.addCell(c);
        }

        int rv = 1;
        for (VentaResponseDto v : r.getVentasRecientes()) {
            boolean alt = rv++ % 2 == 0;
            Color bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
            String fecha = v.getFecha() != null ? v.getFecha().toString().substring(0, 10) : "";
            for (String val : new String[]{
                    "#" + v.getIdVenta(), v.getCliente(), v.getDocumentoCliente(),
                    String.valueOf(v.getDetalles().size()),
                    formatCOP(v.getTotal().doubleValue()), fecha}) {
                PdfPCell c = new PdfPCell(new Phrase(val, fuenteNormal));
                c.setBackgroundColor(bg);
                c.setPadding(5);
                c.setBorder(Rectangle.NO_BORDER);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                ventasTbl.addCell(c);
            }
        }
        doc.add(ventasTbl);


        doc.add(Chunk.NEWLINE);
        Font fuenteFooter = new Font(Font.HELVETICA, 7, Font.ITALIC, Color.GRAY);
        Paragraph footer = new Paragraph("Generado por BikeShop — " + java.time.LocalDateTime.now().toString().substring(0, 16), fuenteFooter);
        footer.setAlignment(Element.ALIGN_RIGHT);
        doc.add(footer);

        doc.close();
        return out.toByteArray();
    }

    private void agregarKpi(PdfPTable table, String label, String value, Font fVal, Font fLbl) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderColor(new Color(220, 220, 220));
        cell.setPadding(10);
        Paragraph val = new Paragraph(value, fVal);
        val.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(val);
        Paragraph lbl = new Paragraph(label, fLbl);
        lbl.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(lbl);
        table.addCell(cell);
    }

    private void agregarFilaTop(PdfPTable table, String pos, String marca, String modelo,
                                 String cantidad, String ingresos, boolean alt, Font f) {
        Color bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
        for (String val : new String[]{pos, marca, modelo, cantidad, ingresos}) {
            PdfPCell c = new PdfPCell(new Phrase(val, f));
            c.setBackgroundColor(bg);
            c.setPadding(5);
            c.setBorder(Rectangle.NO_BORDER);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }
    }

    private String formatCOP(double value) {
        return "$" + String.format("%,.0f", value);
    }
}
