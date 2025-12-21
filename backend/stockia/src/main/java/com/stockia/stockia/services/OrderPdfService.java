package com.stockia.stockia.services;

import com.stockia.stockia.models.Order;

/**
 * Servicio para la generación de PDFs de comprobantes de venta.
 */
public interface OrderPdfService {

    /**
     * Genera un PDF del comprobante de venta para una orden.
     */
    byte[] generatePdf(Order order);
}
