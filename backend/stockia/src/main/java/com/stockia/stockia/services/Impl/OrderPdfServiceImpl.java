package com.stockia.stockia.services.Impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
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
 * Implementación del servicio de generación de PDFs usando iText 7.
 * Genera comprobantes de venta con diseño inspirado en Pro Eat Fit Bar.
 */
@Service
@Slf4j
public class OrderPdfServiceImpl implements OrderPdfService {

        // Datos estáticos del negocio
        private static final String BUSINESS_NAME = "PRO EAT";
        private static final String BUSINESS_SUBTITLE = "FIT BAR";
        private static final String BUSINESS_ADDRESS = "Calle Ejemplo 123, Ciudad, Provincia";
        private static final String BUSINESS_PHONE = "+54 11 1234-5678";
        private static final String DEFAULT_TAX_CONDITION = "Consumidor final";
        private static final String DEFAULT_DNI_CUIT = "N/A";

        private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        private static final DeviceRgb BORDER_COLOR = new DeviceRgb(200, 200, 200); // Gris claro para bordes

        @Override
        public byte[] generatePdf(Order order) {
                log.info("Generating PDF for order: {}", order.getOrderNumber());

                try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PdfWriter writer = new PdfWriter(baos);
                        PdfDocument pdfDoc = new PdfDocument(writer);
                        Document document = new Document(pdfDoc);

                        // Configurar márgenes
                        document.setMargins(40, 40, 40, 40);

                        // Encabezado
                        addHeader(document);

                        // Información del negocio y orden
                        addBusinessAndOrderInfo(document, order);

                        // Datos del cliente
                        addCustomerInfo(document, order);

                        // Tabla de productos
                        addItemsTable(document, order);

                        // Total
                        addTotal(document, order);

                        // Footer
                        addFooter(document);

                        document.close();

                        byte[] pdfBytes = baos.toByteArray();
                        log.info("PDF generated successfully for order: {}", order.getOrderNumber());
                        return pdfBytes;

                } catch (Exception e) {
                        log.error("Error generating PDF for order: {}", order.getOrderNumber(), e);
                        throw new RuntimeException("Error al generar el PDF del comprobante", e);
                }
        }

        private void addHeader(Document document) {
                // Tabla para header: Nombre del negocio (izq) | Título del documento (der)
                Table headerTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                                .useAllAvailableWidth()
                                .setMarginBottom(10);

                // Columna izquierda: Nombre del negocio
                Paragraph businessName = new Paragraph()
                                .add(new com.itextpdf.layout.element.Text(BUSINESS_NAME + "\n")
                                                .setFontSize(22)
                                                .setBold())
                                .add(new com.itextpdf.layout.element.Text(BUSINESS_SUBTITLE)
                                                .setFontSize(11));

                Cell leftCell = new Cell()
                                .add(businessName)
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.LEFT);

                // Columna derecha: Título del documento
                Paragraph title = new Paragraph("Comprobante de pago")
                                .setFontSize(16)
                                .setTextAlignment(TextAlignment.RIGHT);

                Cell rightCell = new Cell()
                                .add(title)
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);

                headerTable.addCell(leftCell);
                headerTable.addCell(rightCell);

                document.add(headerTable);

                // Línea separadora
                document.add(new Paragraph()
                                .setBorderBottom(new SolidBorder(BORDER_COLOR, 1))
                                .setMarginBottom(15));
        }

        private void addBusinessAndOrderInfo(Document document, Order order) {
                Table infoTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                                .useAllAvailableWidth()
                                .setMarginBottom(15);

                // Primera fila: Dirección comercial | Número de contacto
                infoTable.addCell(createInfoCell("Dirección comercial:", BUSINESS_ADDRESS));
                infoTable.addCell(createInfoCell("Número de contacto:", BUSINESS_PHONE));

                // Segunda fila: N° de comprobante | Fecha y hora
                infoTable.addCell(createInfoCell("N° de comprobante:", order.getOrderNumber()));
                infoTable.addCell(createInfoCell("Fecha y hora:", order.getOrderDate().format(DATE_FORMAT)));

                document.add(infoTable);

                // Línea separadora
                document.add(new Paragraph()
                                .setBorderBottom(new SolidBorder(BORDER_COLOR, 1))
                                .setMarginBottom(15));
        }

        private void addCustomerInfo(Document document, Order order) {
                // Título de sección
                Paragraph sectionTitle = new Paragraph("Datos del cliente")
                                .setFontSize(12)
                                .setBold()
                                .setMarginBottom(8);
                document.add(sectionTitle);

                // Tabla con 3 columnas para los datos del cliente en una línea
                Table customerTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1, 1 }))
                                .useAllAvailableWidth()
                                .setMarginBottom(15);

                customerTable.addCell(createInfoCell("Nombre completo:", order.getCustomer().getName()));
                customerTable.addCell(createInfoCell("DNI/CUIT:", DEFAULT_DNI_CUIT));
                customerTable.addCell(createInfoCell("Condición fiscal:", DEFAULT_TAX_CONDITION));

                document.add(customerTable);

                // Línea separadora
                document.add(new Paragraph()
                                .setBorderBottom(new SolidBorder(BORDER_COLOR, 1))
                                .setMarginBottom(15));
        }

        private void addItemsTable(Document document, Order order) {
                // Título de sección
                Paragraph sectionTitle = new Paragraph("Productos vendidos")
                                .setFontSize(12)
                                .setBold()
                                .setMarginBottom(8);
                document.add(sectionTitle);

                // Tabla de productos
                Table itemsTable = new Table(UnitValue.createPercentArray(new float[] { 3, 1, 1.5f, 1.5f }))
                                .useAllAvailableWidth()
                                .setMarginBottom(10);

                // Encabezados
                itemsTable.addCell(createTableHeaderCell("Producto"));
                itemsTable.addCell(createTableHeaderCell("Cantidad"));
                itemsTable.addCell(createTableHeaderCell("Precio unitario"));
                itemsTable.addCell(createTableHeaderCell("Subtotal"));

                // Items
                for (OrderItem item : order.getItems()) {
                        itemsTable.addCell(createProductCell(item.getProduct().getName()));
                        itemsTable.addCell(createQuantityCell(String.valueOf(item.getQuantity())));
                        itemsTable.addCell(createPriceCell(String.format("AR$ %.3f", item.getUnitPrice())));
                        itemsTable.addCell(createPriceCell(String.format("AR$ %.3f", item.getItemTotal())));
                }

                document.add(itemsTable);

                // Subtotal y descuento dentro de la misma tabla
                Table totalsTable = new Table(UnitValue.createPercentArray(new float[] { 3, 1 }))
                                .useAllAvailableWidth()
                                .setMarginBottom(15);

                // Subtotal
                totalsTable.addCell(createLabelCell("Subtotal"));
                totalsTable.addCell(createTotalValueCell(String.format("AR$ %.3f", order.getSubtotal())));

                // Descuento (si aplica)
                if (order.getDiscountAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        // Calcular porcentaje de descuento
                        double discountPercentage = order.getDiscountAmount()
                                        .divide(order.getSubtotal(), 4, java.math.RoundingMode.HALF_UP)
                                        .multiply(java.math.BigDecimal.valueOf(100))
                                        .doubleValue();

                        String discountLabel = String.format("Descuento (%.0f%%)", discountPercentage);
                        totalsTable.addCell(createLabelCell(discountLabel));
                        totalsTable.addCell(
                                        createTotalValueCell(String.format("-AR$ %.3f", order.getDiscountAmount())));
                }

                document.add(totalsTable);

                // Línea separadora
                document.add(new Paragraph()
                                .setBorderBottom(new SolidBorder(BORDER_COLOR, 1))
                                .setMarginBottom(15));
        }

        private void addTotal(Document document, Order order) {
                // Total grande y destacado
                Table totalTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                                .useAllAvailableWidth()
                                .setMarginBottom(30);

                Cell labelCell = new Cell()
                                .add(new Paragraph("Total")
                                                .setFontSize(18)
                                                .setBold())
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.LEFT);

                Cell valueCell = new Cell()
                                .add(new Paragraph(String.format("AR$ %.3f", order.getTotalAmount()))
                                                .setFontSize(18)
                                                .setBold())
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);

                totalTable.addCell(labelCell);
                totalTable.addCell(valueCell);

                document.add(totalTable);
        }

        private void addFooter(Document document) {
                // Footer: "Powered by" en primera línea
                Paragraph poweredBy = new Paragraph("Powered by")
                                .setFontSize(9)
                                .setFontColor(ColorConstants.GRAY)
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setMarginTop(20)
                                .setMarginBottom(2);
                document.add(poweredBy);

                // "stockia" en línea separada con fuente más grande
                Paragraph stockia = new Paragraph("stockia")
                                .setFontSize(24)
                                .setBold()
                                .setFontColor(ColorConstants.BLACK)
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setMarginTop(0);
                document.add(stockia);
        }

        // ==================== Helper Methods ====================

        private Cell createInfoCell(String label, String value) {
                Paragraph content = new Paragraph()
                                .add(new com.itextpdf.layout.element.Text(label + " ")
                                                .setFontSize(9)
                                                .setBold())
                                .add(new com.itextpdf.layout.element.Text(value)
                                                .setFontSize(9));

                return new Cell()
                                .add(content)
                                .setBorder(Border.NO_BORDER)
                                .setPaddingBottom(5);
        }

        private Cell createTableHeaderCell(String text) {
                return new Cell()
                                .add(new Paragraph(text)
                                                .setFontSize(10)
                                                .setBold())
                                .setBorder(Border.NO_BORDER)
                                .setBorderBottom(new SolidBorder(BORDER_COLOR, 1))
                                .setPadding(5)
                                .setTextAlignment(TextAlignment.LEFT);
        }

        private Cell createProductCell(String text) {
                return new Cell()
                                .add(new Paragraph(text)
                                                .setFontSize(9))
                                .setBorder(Border.NO_BORDER)
                                .setPaddingTop(5)
                                .setPaddingBottom(5)
                                .setTextAlignment(TextAlignment.LEFT);
        }

        private Cell createQuantityCell(String text) {
                return new Cell()
                                .add(new Paragraph(text)
                                                .setFontSize(9))
                                .setBorder(Border.NO_BORDER)
                                .setPaddingTop(5)
                                .setPaddingBottom(5)
                                .setTextAlignment(TextAlignment.CENTER);
        }

        private Cell createPriceCell(String text) {
                return new Cell()
                                .add(new Paragraph(text)
                                                .setFontSize(9))
                                .setBorder(Border.NO_BORDER)
                                .setPaddingTop(5)
                                .setPaddingBottom(5)
                                .setTextAlignment(TextAlignment.RIGHT);
        }

        private Cell createLabelCell(String text) {
                return new Cell()
                                .add(new Paragraph(text)
                                                .setFontSize(10))
                                .setBorder(Border.NO_BORDER)
                                .setPaddingTop(5)
                                .setPaddingBottom(5)
                                .setTextAlignment(TextAlignment.RIGHT);
        }

        private Cell createTotalValueCell(String text) {
                return new Cell()
                                .add(new Paragraph(text)
                                                .setFontSize(10))
                                .setBorder(Border.NO_BORDER)
                                .setPaddingTop(5)
                                .setPaddingBottom(5)
                                .setTextAlignment(TextAlignment.RIGHT);
        }
}
