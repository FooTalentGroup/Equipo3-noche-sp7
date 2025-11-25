package com.stockia.stockia.services.Impl;

import com.stockia.stockia.models.Order;
import com.stockia.stockia.models.OrderItem;
import com.stockia.stockia.services.OrderPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

/**
 * Implementación del servicio de generación de PDFs.
 * Genera comprobantes de venta en formato de texto estructurado.
 * 
 * NOTA: Esta es una implementación básica. Para producción, se recomienda
 * integrar una biblioteca de PDF como iText o Apache PDFBox.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Service
@Slf4j
public class OrderPdfServiceImpl implements OrderPdfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public byte[] generatePdf(Order order) {
        log.info("Generating PDF for order: {}", order.getOrderNumber());

        try {
            StringBuilder content = new StringBuilder();

            // Encabezado
            content.append("========================================\\n");
            content.append("       COMPROBANTE DE VENTA\\n");
            content.append("          STOCKIA SYSTEM\\n");
            content.append("========================================\\n\\n");

            // Información de la orden
            content.append(String.format("Orden Nº: %s\\n", order.getOrderNumber()));
            content.append(String.format("Fecha: %s\\n", order.getOrderDate().format(DATE_FORMAT)));
            content.append(String.format("Estado: %s\\n", order.getStatus()));
            content.append("------------------------------------------------------------------------\\n\\n");

            // Información del cliente
            content.append("DATOS DEL CLIENTE:\\n");
            content.append(String.format("Nombre: %s\\n", order.getCustomer().getName()));
            content.append(String.format("Email: %s\\n", order.getCustomer().getEmail()));
            content.append(String.format("Teléfono: %s\\n", order.getCustomer().getPhone()));
            content.append("------------------------------------------------------------------------\\n\\n");

            // Detalle de productos
            content.append("DETALLE DE PRODUCTOS:\\n\\n");
            content.append(String.format("%-40s %8s %12s %14s\\n", "Producto", "Cant.", "Precio Unit.", "Total"));
            content.append("------------------------------------------------------------------------\\n");

            for (OrderItem item : order.getItems()) {
                content.append(String.format("%-40s %8d $%11.2f $%13.2f\\n",
                        truncate(item.getProduct().getName(), 40),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getItemTotal()));
            }

            content.append("------------------------------------------------------------------------\\n\\n");

            // Totales
            content.append(String.format("%54s $%13.2f\\n", "Subtotal:", order.getSubtotal()));
            content.append(String.format("%54s $%13.2f\\n", "Descuento:", order.getDiscountAmount()));
            content.append(String.format("%54s $%13.2f\\n", "TOTAL:", order.getTotalAmount()));
            content.append("========================================================================\\n\\n");

            // Información de pago
            content.append("INFORMACIÓN DE PAGO:\\n");
            content.append(String.format("Método de pago: %s\\n", order.getPaymentMethod()));
            content.append(String.format("Estado del pago: %s\\n", order.getPaymentStatus()));
            if (order.getPaymentNote() != null && !order.getPaymentNote().isEmpty()) {
                content.append(String.format("Nota: %s\\n", order.getPaymentNote()));
            }
            content.append("\\n");

            // Información del vendedor
            content.append("------------------------------------------------------------------------\\n");
            content.append(String.format("Atendido por: %s\\n", order.getUser().getName()));

            if (order.getDeliveredDate() != null) {
                content.append(String.format("Entregado: %s\\n", order.getDeliveredDate().format(DATE_FORMAT)));
            }

            content.append("\\n========================================\\n");
            content.append("      ¡Gracias por su compra!\\n");
            content.append("========================================\\n");

            // Convertir a bytes
            byte[] pdfBytes = content.toString().getBytes(StandardCharsets.UTF_8);

            log.info("PDF generated successfully for order: {}", order.getOrderNumber());
            return pdfBytes;

        } catch (Exception e) {
            log.error("Error generating PDF for order: {}", order.getOrderNumber(), e);
            throw new RuntimeException("Error al generar el PDF del comprobante", e);
        }
    }

    /**
     * Trunca un texto al tamaño especificado.
     */
    private String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}
