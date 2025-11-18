import { useState, useMemo } from 'react';
import { MoreHorizontal, Trash } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';

export const ProductsTable = ({ searchQuery = '' }) => {
    const [products, setProducts] = useState(
        Array.from({ length: 20 }, (_, index) => ({
            id: index + 1,
            producto: `Jugo de naranja ${index + 1}`,
            categoria: 'Bebidas',
            costo: '$1200',
            pecio: '$2400',
            descuento: '10',
            stock_actual: '20',
            stock_minimo: '8',
            proveedor: 'John Doe'
        }))
    );

    const handleDelete = id => {
        setProducts(prev => prev.filter(product => product.id !== id));
    };

    const handleMoreClick = product => {
        console.log('More actions for:', product);
    };

    const filteredProducts = useMemo(() => {
        const term = searchQuery.trim().toLowerCase();
        if (!term) return products;
        return products.filter(product =>
            product.producto.toLowerCase().includes(term)
        );
    }, [products, searchQuery]);

    return (
        <div className='overflow-x-auto bg-white shadow-sm rounded-lg border border-gray-200'>
            <table className='min-w-full text-sm'>
                <thead className='bg-gray-50'>
                <tr>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Producto
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Categoria
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Costo
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Pecio
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Descuento
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Stock Actual
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Stock Minimo
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Proveedor
                    </th>
                    <th className='px-4 py-3 text-left font-semibold text-gray-700'>
                        Accion
                    </th>
                </tr>
                </thead>
                <tbody className='divide-y divide-gray-100'>
                {filteredProducts.map(product => (
                    <tr key={product.id} className='hover:bg-gray-50'>
                        <td className='px-4 py-3 text-gray-900'>{product.producto}</td>
                        <td className='px-4 py-3 text-gray-700'>{product.categoria}</td>
                        <td className='px-4 py-3 text-gray-700'>{product.costo}</td>
                        <td className='px-4 py-3 text-gray-700'>{product.pecio}</td>
                        <td className='px-4 py-3 text-gray-700'>
                            {product.descuento}%
                        </td>
                        <td className='px-4 py-3 text-gray-700'>
                            {product.stock_actual}
                        </td>
                        <td className='px-4 py-3 text-gray-700'>
                            {product.stock_minimo}
                        </td>
                        <td className='px-4 py-3 text-gray-700'>
                            {product.proveedor}
                        </td>
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
                        <td
                            colSpan={9}
                            className='px-4 py-6 text-center text-gray-500 text-sm'
                        >
                            No hay productos para mostrar.
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};