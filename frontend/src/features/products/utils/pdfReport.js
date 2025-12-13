import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

const formatDate = (isoDate) => {
  const date = new Date(isoDate);
  if (isNaN(date)) return 'N/A';
  return date.toLocaleDateString('es-AR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
};

const formatPrice = (price) => {
  const numericPrice = parseFloat(price);
  if (isNaN(numericPrice)) return 'AR$ 0,00';
  return `AR$ ${numericPrice.toFixed(2).replace('.', ',').replace(/\B(?=(\d{3})+(?!\d))/g, '.')}`;
};

const generarReportePDF = (productos) => {

  if (!productos || productos.length === 0) {
    alert("No hay productos disponibles para generar el reporte.");
    return;
  }

  const doc = new jsPDF();
  const margenX = 15;
  let currentY = 20;

  // 1. Título y Estilo del Reporte
  doc.setFontSize(24);
  doc.setFont('helvetica', 'normal');
  doc.text('Reporte de Productos', 105, currentY, { align: 'center' });
  currentY += 10;

  doc.setLineWidth(0.5);
  doc.line(margenX, currentY, 210 - margenX, currentY);
  currentY += 10;

  doc.setFontSize(10);
  doc.setFont('helvetica', 'bold');
  doc.text('Fecha de Reporte:', margenX, currentY);
  doc.setFont('helvetica', 'normal');
  doc.text(new Date().toLocaleDateString('es-AR'), margenX + 30, currentY);
  currentY += 10;

  doc.setFontSize(14);
  doc.setFont('helvetica', 'bold');
  doc.text('Detalle de Productos', margenX, currentY);
  currentY += 5;

  // 2. Preparación de los datos y la tabla
  const headers = ["Producto", "Categoría", "Precio", "Stock Actual", "Stock Mínimo", "Fecha Creación"];

  const body = productos.map(p => [
    p.name,
    p.category.name,
    formatPrice(p.price),
    p.currentStock,
    p.minStock,
    formatDate(p.createdAt),
  ]);

  // Generación de la tabla
  autoTable(doc, {
    startY: currentY + 5,
    head: [headers],
    body: body,
    theme: 'striped',
    styles: {
      fontSize: 8,
      cellPadding: 2,
      valign: 'middle',
    },
    headStyles: {
      fillColor: [255, 255, 255],
      textColor: [0, 0, 0],
      fontStyle: 'bold',
      lineWidth: 0.1,
      lineColor: [0, 0, 0],
      halign: 'center'
    },
    columnStyles: {
      0: { cellWidth: 50, halign: 'left' },
      1: { cellWidth: 25, halign: 'center' },
      2: { cellWidth: 20, halign: 'right' },
      3: { cellWidth: 20, halign: 'center' },
      4: { cellWidth: 20, halign: 'center' },
      5: { cellWidth: 25, halign: 'center' },
    },
    margin: { left: margenX, right: margenX },
  });

  doc.save(`reporte_productos_inventario_${new Date().toLocaleDateString('es-AR')}.pdf`);
};

export default generarReportePDF;