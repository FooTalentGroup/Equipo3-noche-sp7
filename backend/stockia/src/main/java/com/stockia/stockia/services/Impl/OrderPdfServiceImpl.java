package com.stockia.stockia.services.Impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.stockia.stockia.models.Order;
import com.stockia.stockia.models.OrderItem;
import com.stockia.stockia.services.OrderPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Implementación mejorada del servicio de generación de PDFs usando iText 7.
 * Genera comprobantes de venta profesionales en formato PDF.
 *
 * @author StockIA Team
 * @version 2.0
 * @since 2025-11-25
 */
@Service
@Slf4j
public class OrderPdfServiceImpl implements OrderPdfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(41, 128, 185); // Azul profesional
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(52, 73, 94); // Gris oscuro

    @Override
    public byte[] generatePdf(Order order) {
        log.info("Generating professional PDF for order: {}", order.getOrderNumber());

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Configurar márgenes
            document.setMargins(40, 40, 40, 40);

            // Encabezado con logo y título
            addHeader(document, order);

            // Información de la orden
            addOrderInfo(document, order);

            // Información del cliente
            addCustomerInfo(document, order);

            // Tabla de productos
            addItemsTable(document, order);

            // Totales
            addTotalsSection(document, order);

            // Información de pago
            addPaymentInfo(document, order);

            // Footer
            addFooter(document, order);

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            log.info("Professional PDF generated successfully for order: {}", order.getOrderNumber());
            return pdfBytes;

        } catch (Exception e) {
            log.error("Error generating PDF for order: {}", order.getOrderNumber(), e);
            throw new RuntimeException("Error al generar el PDF del comprobante", e);
        }
    }

    private void addHeader(Document document, Order order) {
        // Título principal
        Paragraph title = new Paragraph("COMPROBANTE DE VENTA")
                .setFontSize(24)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(title);

        // Subtítulo
        Paragraph subtitle = new Paragraph("STOCKIA SYSTEM")
                .setFontSize(12)
                .setFontColor(SECONDARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitle);

        // Línea separadora
        document.add(new Paragraph()
                .setBorderTop(new com.itextpdf.layout.borders.SolidBorder(PRIMARY_COLOR, 2))
                .setMarginBottom(20));
    }

    private void addOrderInfo(Document document, Order order) {
        Table infoTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        // Columna izquierda
        infoTable.addCell(createInfoCell("Orden Nº:", order.getOrderNumber(), true));
        infoTable.addCell(createInfoCell("Fecha:", order.getOrderDate().format(DATE_FORMAT), false));

        // Columna derecha
        infoTable.addCell(createInfoCell("Estado:", order.getStatus().toString(), true));
        infoTable.addCell(createInfoCell("Pago:", order.getPaymentStatus().toString(), false));

        document.add(infoTable);
    }

    private void addCustomerInfo(Document document, Order order) {
        Paragraph sectionTitle = new Paragraph("DATOS DEL CLIENTE")
                .setFontSize(12)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setMarginTop(10)
                .setMarginBottom(10);
        document.add(sectionTitle);

        Table customerTable = new Table(UnitValue.createPercentArray(new float[] { 1, 2 }))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        customerTable.addCell(createLabelCell("Nombre:"));
        customerTable.addCell(createValueCell(order.getCustomer().getName()));

        customerTable.addCell(createLabelCell("Email:"));
        customerTable.addCell(createValueCell(order.getCustomer().getEmail()));

        customerTable.addCell(createLabelCell("Teléfono:"));
        customerTable.addCell(createValueCell(order.getCustomer().getPhone()));

        document.add(customerTable);
    }

    private void addItemsTable(Document document, Order order) {
        Paragraph sectionTitle = new Paragraph("DETALLE DE PRODUCTOS")
                .setFontSize(12)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setMarginTop(10)
                .setMarginBottom(10);
        document.add(sectionTitle);

        Table itemsTable = new Table(UnitValue.createPercentArray(new float[] { 3, 1, 1.5f, 1.5f }))
                .useAllAvailableWidth();

        // Encabezados
        itemsTable.addHeaderCell(createHeaderCell("Producto"));
        itemsTable.addHeaderCell(createHeaderCell("Cant."));
        itemsTable.addHeaderCell(createHeaderCell("Precio Unit."));
        itemsTable.addHeaderCell(createHeaderCell("Total"));

        // Items
        for (OrderItem item : order.getItems()) {
            itemsTable.addCell(createItemCell(item.getProduct().getName()));
            itemsTable.addCell(createItemCell(String.valueOf(item.getQuantity())));
            itemsTable.addCell(createItemCell(String.format("$%.2f", item.getUnitPrice())));
            itemsTable.addCell(createItemCell(String.format("$%.2f", item.getItemTotal())));
        }

        document.add(itemsTable);
    }

    private void addTotalsSection(Document document, Order order) {
        Table totalsTable = new Table(UnitValue.createPercentArray(new float[] { 3, 1 }))
                .useAllAvailableWidth()
                .setMarginTop(15)
                .setMarginBottom(15);

        // Subtotal
        totalsTable.addCell(createTotalLabelCell("Subtotal:"));
        totalsTable.addCell(createTotalValueCell(String.format("$%.2f", order.getSubtotal())));

        // Descuento
        if (order.getDiscountAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            totalsTable.addCell(createTotalLabelCell("Descuento:"));
            totalsTable.addCell(createTotalValueCell(String.format("- $%.2f", order.getDiscountAmount())));
        }

        // Total
        totalsTable.addCell(createTotalLabelCell("TOTAL:").setBold().setFontSize(14));
        totalsTable.addCell(createTotalValueCell(String.format("$%.2f", order.getTotalAmount()))
                .setBold()
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR));

        document.add(totalsTable);
    }

    private void addPaymentInfo(Document document, Order order) {
        Paragraph sectionTitle = new Paragraph("INFORMACIÓN DE PAGO")
                .setFontSize(12)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setMarginTop(10)
                .setMarginBottom(10);
        document.add(sectionTitle);

        Table paymentTable = new Table(UnitValue.createPercentArray(new float[] { 1, 2 }))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        paymentTable.addCell(createLabelCell("Método de pago:"));
        paymentTable.addCell(createValueCell(order.getPaymentMethod().toString()));

        if (order.getPaymentNote() != null && !order.getPaymentNote().isEmpty()) {
            paymentTable.addCell(createLabelCell("Nota:"));
            paymentTable.addCell(createValueCell(order.getPaymentNote()));
        }

        document.add(paymentTable);
    }

    private void addFooter(Document document, Order order) {
        // Información del vendedor
        Paragraph vendorInfo = new Paragraph(String.format("Atendido por: %s", order.getUser().getName()))
                .setFontSize(10)
                .setFontColor(SECONDARY_COLOR)
                .setMarginTop(20);
        document.add(vendorInfo);

        if (order.getDeliveredDate() != null) {
            Paragraph deliveryInfo = new Paragraph(
                    String.format("Entregado: %s", order.getDeliveredDate().format(DATE_FORMAT)))
                    .setFontSize(10)
                    .setFontColor(SECONDARY_COLOR);
            document.add(deliveryInfo);
        }

        // Mensaje de agradecimiento
        Paragraph thanks = new Paragraph("¡Gracias por su compra!")
                .setFontSize(12)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30);
        document.add(thanks);
    }

    // Helper methods para crear celdas con estilos consistentes

    private Cell createInfoCell(String label, String value, boolean isLeft) {
        Paragraph content = new Paragraph()
                .add(new com.itextpdf.layout.element.Text(label + " ").setBold())
                .add(value);

        return new Cell()
                .add(content)
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(5);
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(PRIMARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
    }

    private Cell createItemCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(10))
                .setPadding(5)
                .setTextAlignment(TextAlignment.LEFT);
    }

    private Cell createLabelCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(5);
    }

    private Cell createValueCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(5);
    }

    private Cell createTotalLabelCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPaddingRight(10);
    }

    private Cell createTotalValueCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }
}
