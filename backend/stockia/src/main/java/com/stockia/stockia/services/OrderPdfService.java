package com.stockia.stockia.services;

import com.stockia.stockia.models.Order;

/**
 * Servicio para la generación de PDFs de comprobantes de venta.
 * Genera documentos PDF con los detalles completos de las órdenes.
 */
public interface OrderPdfService {

    /**
     * Genera un PDF del comprobante de venta para una orden.
     * 
     * @param order Orden para la cual generar el PDF
     * @return Bytes del archivo PDF generado
     */
    byte[] generatePdf(Order order);
}
