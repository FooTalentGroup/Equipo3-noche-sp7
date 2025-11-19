import { useState, useMemo } from 'react';
import { MoreHorizontal, Trash } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';

// Helper to parse prices like "$2.400" → 2400
const parsePrice = (val) => {
    if (val == null) return 0;
    if (typeof val === 'number') return val;
    const num = String(val).replace(/[^0-9.-]+/g, '');
    const parsed = Number(num);
    return Number.isFinite(parsed) ? parsed : 0;
};

// normalize helper: remove diacritics + lowercase for robust comparisons
const normalize = (s) =>
    String(s || '')
        .normalize?.('NFD')
        .replace(/\p{Diacritic}/gu, '') // remove accents
        .toLowerCase();

export const ProductsTable = ({
    searchQuery = '',
    filters = {},   // { category, minPrice, maxPrice, inStock }
    sort = 'name_asc' // name_asc | name_desc | price_asc | price_desc
}) => {

    // === Realistic random data generation ===
    const productNames = [
        'Jugo de Naranja Natural', 'Leche Descremada', 'Pan de Molde Integral', 'Manzanas Rojas', 'Yogurt Natural',
        'Cereal Corn Flakes', 'Agua Mineral Sin Gas', 'Galletitas de Agua', 'Queso Cremoso', 'Mermelada de Durazno',
        'Aceite de Oliva', 'Arroz Largo Fino', 'Fideos Spaghetti', 'Café Torrado', 'Gaseosa Cola 1.5L',
        'Detergente Líquido', 'Shampoo Reparador', 'Jabón en Barra', 'Papel Higiénico Doble Hoja', 'Galletas Chocolate'
    ];

    const categories = ['Bebidas', 'Lácteos', 'Panadería', 'Frutas y Verduras', 'Desayuno', 'Almacén', 'Limpieza', 'Cuidado Personal'];

    const suppliers = ['Distribuidora del Norte', 'Alimentos Sur SRL', 'Proveeduría Central', 'Importadora Este', 'Mayorista Oeste', 'Cooperativa Láctea'];

    const randomItem = (arr) => arr[Math.floor(Math.random() * arr.length)];
    const randomInRange = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min;

    const generateRandomProducts = () => {
        return Array.from({ length: 30 }, (_, i) => {
            const costo = randomInRange(800, 12000);
            const markup = randomInRange(70, 180); // 70%–180% markup
            const precio = Math.round(costo * (1 + markup / 100));
            const hasDiscount = Math.random() > 0.55;
            const descuento = hasDiscount ? randomInRange(5, 35) : 0;

            const stockActual = randomInRange(0, 150);
            const stockMinimo = randomInRange(5, 30);

            return {
                id: i + 1,
                producto: randomItem(productNames),
                categoria: randomItem(categories),
                costo: `$${costo.toLocaleString('es-AR')}`,
                precio: `$${precio.toLocaleString('es-AR')}`,
                descuento: descuento > 0 ? descuento : 0,
                stock_actual: stockActual,
                stock_minimo: stockMinimo,
                proveedor: randomItem(suppliers),
            };
        });
    };

    const [products, setProducts] = useState(generateRandomProducts());

    // Delete handler
    const handleDelete = (id) => {
        setProducts(prev => prev.filter(p => p.id !== id));
    };

    const handleMoreClick = (product) => {
        console.log('Acciones adicionales para:', product);
    };

    // === Filtering & Sorting ===
    const filteredProducts = useMemo(() => {
        const term = (searchQuery || '').trim().toLowerCase();
        // support new stockStatus or fall back to old inStock boolean
        const {
            category = 'all',
            minPrice = '',
            maxPrice = '',
            stockStatus = undefined,
            inStock = false
        } = filters || {};

        const effectiveStock = stockStatus ?? (inStock ? 'in' : 'all');

        let result = [...products];

        // Search in name, category and supplier
        if (term) {
            result = result.filter(p =>
                p.producto.toLowerCase().includes(term) ||
                p.categoria.toLowerCase().includes(term) ||
                p.proveedor.toLowerCase().includes(term)
            );
        }

        // Category filter (normalized)
        if (category && category !== 'all') {
            const catNorm = normalize(category);
            result = result.filter(p => normalize(p.categoria) === catNorm);
        }

        // Price filters
        const min = minPrice === '' ? null : parseFloat(minPrice);
        const max = maxPrice === '' ? null : parseFloat(maxPrice);

        if (min !== null && !Number.isNaN(min)) {
            result = result.filter(p => parsePrice(p.precio) >= min);
        }
        if (max !== null && !Number.isNaN(max)) {
            result = result.filter(p => parsePrice(p.precio) <= max);
        }

        // Stock filters: 'in' -> >0, 'out' -> ===0, 'all' -> no filter
        if (effectiveStock === 'in') {
            result = result.filter(p => Number(p.stock_actual ?? 0) > 0);
        } else if (effectiveStock === 'out') {
            result = result.filter(p => Number(p.stock_actual ?? 0) === 0);
        }

        // Sorting
        switch (sort) {
            case 'name_asc':
                result.sort((a, b) => a.producto.localeCompare(b.producto));
                break;
            case 'name_desc':
                result.sort((a, b) => b.producto.localeCompare(a.producto));
                break;
            case 'price_asc':
                result.sort((a, b) => parsePrice(a.precio) - parsePrice(b.precio));
                break;
            case 'price_desc':
                result.sort((a, b) => parsePrice(b.precio) - parsePrice(a.precio));
                break;
            default:
                break;
        }

        return result;
    }, [products, searchQuery, filters, sort]);

    return (
        <div className='overflow-x-auto bg-white shadow-sm rounded-lg border border-gray-200'>
            <table className='min-w-full text-sm'>
                <thead className='bg-gray-50'>
                    <tr>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Producto</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Categoría</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Costo</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Precio</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Descuento</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Stock Actual</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Stock Mínimo</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Proveedor</th>
                        <th className='px-4 py-3 text-left font-semibold text-gray-700'>Acciones</th>
                    </tr>
                </thead>
                <tbody className='divide-y divide-gray-100'>
                    {filteredProducts.map(product => (
                        <tr key={product.id} className='hover:bg-gray-50'>
                            <td className='px-4 py-3 text-gray-900 font-medium'>{product.producto}</td>
                            <td className='px-4 py-3 text-gray-700'>{product.categoria}</td>
                            <td className='px-4 py-3 text-gray-700'>{product.costo}</td>
                            <td className='px-4 py-3 text-gray-700 font-semibold'>{product.precio}</td>
                            <td className='px-4 py-3 text-gray-700'>
                                {product.descuento > 0 ? `${product.descuento}%` : '-'}
                            </td>
                            <td className='px-4 py-3 text-gray-700'>
                                <span className={product.stock_actual <= product.stock_minimo ? 'text-red-600 font-medium' : ''}>
                                    {product.stock_actual}
                                </span>
                            </td>
                            <td className='px-4 py-3 text-gray-700'>{product.stock_minimo}</td>
                            <td className='px-4 py-3 text-gray-700'>{product.proveedor}</td>
                            <td className='px-4 py-3'>
                                <div className='flex items-center gap-2'>
                                    <Button
                                        variant='ghost'
                                        size='icon'
                                        className='h-8 w-8'
                                        onClick={() => handleMoreClick(product)}
                                    >
                                        <MoreHorizontal className='h-4 w-4' />
                                    </Button>
                                    <Button
                                        variant='destructive'
                                        size='icon'
                                        className='h-8 w-8'
                                        onClick={() => handleDelete(product.id)}
                                    >
                                        <Trash className='h-4 w-4' />
                                    </Button>
                                </div>
                            </td>
                        </tr>
                    ))}

                    {filteredProducts.length === 0 && (
                        <tr>
                            <td colSpan={9} className='px-4 py-10 text-center text-gray-500'>
                                No se encontraron productos con los filtros aplicados.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};