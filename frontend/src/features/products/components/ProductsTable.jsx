// ProductsTable.jsx
import { useState, useMemo } from 'react';
import { Edit, Trash2, ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from '@/shared/components/ui/button.jsx';
import Badge from "@/features/products/components/Badge.jsx";
// =============== HELPERS (NO BORRAR) ===============
const parsePrice = (val) => {
    if (val == null) return 0;
    if (typeof val === 'number') return val;
    const num = String(val).replace(/[^0-9.-]+/g, '');
    const parsed = Number(num);
    return Number.isFinite(parsed) ? parsed : 0;
};

const normalize = (s) => String(s || '')
    .normalize?.('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .toLowerCase();

const productNames = [
    'Jugo de Naranja Natural', 'Leche Descremada', 'Pan de Molde Integral', 'Manzanas Rojas',
    'Yogurt Natural', 'Cereal Corn Flakes', 'Agua Mineral Sin Gas', 'Galletitas de Agua',
    'Queso Cremoso', 'Mermelada de Durazno', 'Aceite de Oliva', 'Arroz Largo Fino',
    'Fideos Spaghetti', 'Café Torrado', 'Gaseosa Cola 1.5L', 'Detergente Líquido',
    'Shampoo Reparador', 'Jabón en Barra', 'Papel Higiénico Doble Hoja', 'Galletas Chocolate'
];

const categories = ['Bebidas', 'Lácteos', 'Panadería', 'Frutas y Verduras', 'Desayuno', 'Almacén', 'Limpieza', 'Cuidado Personal'];
const suppliers = ['Distribuidora del Norte', 'Alimentos Sur SRL', 'Proveeduría Central', 'Importadora Este', 'Mayorista Oeste', 'Cooperativa Láctea'];

const randomItem = (arr) => arr[Math.floor(Math.random() * arr.length)];
const randomInRange = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

const generateRandomProducts = () => {
    return Array.from({ length: 50 }, (_, i) => {
        const costo = randomInRange(800, 12000);
        const markup = randomInRange(70, 180);
        const precio = Math.round(costo * (1 + markup / 100));
        const hasDiscount = Math.random() > 0.55;
        const descuento = hasDiscount ? randomInRange(5, 35) : 0;
        const stockActual = randomInRange(0, 150);
        const stockMinimo = randomInRange(5, 30);

        return {
            id: i + 1,
            producto: randomItem(productNames),
            categoria: randomItem(categories),
            precio: `$${precio.toLocaleString('es-AR')}`,
            descuento,
            stock_actual: stockActual,
            stock_minimo: stockMinimo,
            proveedor: randomItem(suppliers),
        };
    });
};
// ====================================================

export const ProductsTable = ({ searchQuery = '', filters = {}, sort = 'name_asc' }) => {
    const [products, setProducts] = useState(generateRandomProducts());
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const handleDelete = (id) => {
        setProducts(prev => prev.filter(p => p.id !== id));
    };

    const getStockBadge = (actual, minimo) => {
        if (actual === 0) {
            return <Badge variant="destructive">Sin stock</Badge>;
        }
        if (actual <= 10) { // ← Tus reglas: 1 a 10 = Bajo stock
            return <Badge variant="warning">Bajo stock</Badge>;
        }
        return <Badge variant="success">Alto stock</Badge>; // > 10 = Alto stock
    };

    // Filtrado y ordenamiento (tu lógica original, solo simplificada un poco)
    const filteredProducts = useMemo(() => {
        let result = [...products];

        // Búsqueda
        if (searchQuery) {
            const term = searchQuery.toLowerCase();
            result = result.filter(p =>
                p.producto.toLowerCase().includes(term) ||
                p.categoria.toLowerCase().includes(term) ||
                p.proveedor.toLowerCase().includes(term)
            );
        }

        // Filtros de categoría y stock (los que ya tenías)
        if (filters.category && filters.category !== 'all') {
            const catNorm = normalize(filters.category);
            result = result.filter(p => normalize(p.categoria) === catNorm);
        }

        if (filters.stockStatus === 'in') result = result.filter(p => p.stock_actual > 0);
        if (filters.stockStatus === 'out') result = result.filter(p => p.stock_actual === 0);

        // Ordenamiento
        switch (sort) {
            case 'name_asc': result.sort((a, b) => a.producto.localeCompare(b.producto)); break;
            case 'name_desc': result.sort((a, b) => b.producto.localeCompare(a.producto)); break;
            case 'price_asc': result.sort((a, b) => parsePrice(a.precio) - parsePrice(b.precio)); break;
            case 'price_desc': result.sort((a, b) => parsePrice(b.precio) - parsePrice(a.precio)); break;
        }

        return result;
    }, [products, searchQuery, filters, sort]);

    // Paginación
    const totalPages = Math.ceil(filteredProducts.length / itemsPerPage);
    const paginated = filteredProducts.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);

    return (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
            <div className="overflow-x-auto">
                <table className="w-full text-sm text-left">
                    <thead className="text-xs uppercase bg-gray-50 text-gray-600">
                    <tr>
                        <th className="px-6 py-3">Estado</th>
                        <th className="px-6 py-3">Producto</th>
                        <th className="px-6 py-3">Categoría</th>
                        <th className="px-6 py-3">Precio</th>
                        <th className="px-6 py-3">Descuento</th>
                        <th className="px-6 py-3 text-center">Acciones</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {paginated.map(product => (
                        <tr key={product.id} className="hover:bg-gray-50 transition-colors">
                            <td className="px-6 py-4">
                                <div className="w-28"> {/* Ancho fijo para que todos queden alineados */}
                                    {getStockBadge(product.stock_actual, product.stock_minimo)}
                                </div>
                            </td>
                            <td className="px-6 py-4 font-medium text-gray-900">{product.producto}</td>
                            <td className="px-6 py-4 text-gray-600">{product.categoria}</td>
                            <td className="px-6 py-4 font-semibold text-gray-900">{product.precio}</td>
                            <td className="px-6 py-4">
                                {product.descuento > 0 ? (
                                    <span className="text-green-600 font-medium">{product.descuento}%</span>
                                ) : (
                                    <span className="text-gray-400">0%</span>
                                )}
                            </td>
                            <td className="px-6 py-4">
                                <div className="flex items-center justify-center gap-4">
                                    <button className="text-gray-500 hover:text-blue-600 cursor-pointer transition">
                                        <Edit className="w-4 h-4" />
                                    </button>
                                    <button onClick={() => handleDelete(product.id)} className="text-red-600 cursor-pointer transition">
                                        <Trash2 className="w-4 h-4" />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {/* Paginación */}
            <div className="flex items-center justify-center px-6 py-3 bg-gray-50 border-t">
                <div className="flex items-center gap-2">
                    <button
                        onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
                        disabled={currentPage === 1}
                        className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                    >
                        <ChevronLeft className="w-4 h-4" /> Previous
                    </button>

                    <div className="flex gap-1 justify-center">
                        {Array.from({ length: totalPages }, (_, i) => i + 1).map(page => (
                            <button
                                key={page}
                                onClick={() => setCurrentPage(page)}
                                className={`px-3 py-1.5 text-sm rounded-md ${currentPage === page ? 'bg-slate-600 text-white' : 'text-gray-700 hover:bg-gray-200'}`}
                            >
                                {page}
                            </button>
                        ))}
                    </div>

                    <button
                        onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
                        disabled={currentPage === totalPages}
                        className="border-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-200 rounded-md disabled:opacity-50 flex items-center gap-1"
                    >
                        Next <ChevronRight className="w-4 h-4" />
                    </button>
                </div>
            </div>
        </div>
    );
};