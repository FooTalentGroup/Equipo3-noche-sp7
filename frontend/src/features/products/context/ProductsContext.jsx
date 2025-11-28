import initialCategories from '@/features/categories/mockedCategories';
import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { getProducts } from '../services/productService';

// =============== HELPERS ===============
const productNames = [
    'Jugo de Naranja Natural', 'Leche Descremada', 'Pan de Molde Integral', 'Manzanas Rojas',
    'Yogurt Natural', 'Cereal Corn Flakes', 'Agua Mineral Sin Gas', 'Galletitas de Agua',
    'Queso Cremoso', 'Mermelada de Durazno', 'Aceite de Oliva', 'Arroz Largo Fino',
    'Fideos Spaghetti', 'Café Torrado', 'Gaseosa Cola 1.5L', 'Detergente Líquido',
    'Shampoo Reparador', 'Jabón en Barra', 'Papel Higiénico Doble Hoja', 'Galletas Chocolate'
];

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
            name: randomItem(productNames),
            category: randomItem(initialCategories.map(c => c.name)),
            price: `$${precio.toLocaleString('es-AR')}`,
            descuento,
            stock_actual: stockActual,
            min_stock: stockMinimo,
            proveedor: randomItem(suppliers),
        };
    });
};

// =============== CONTEXT ===============
const ProductsContext = createContext(undefined);

export function ProductsProvider({ children }) {
    const [products, setProducts] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [pagination, setPagination] = useState({
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        pageSize: 10,
    });

    const fetchProducts = useCallback(async (params = {}) => {
        setIsLoading(true);
        try {
            const response = await getProducts({
                page: params.page ?? 0,
                size: params.size ?? 10,
                sort: params.sort,
                q: params.q || params.searchQuery,
                categoryId: params.categoryId,
                lowStock: params.lowStock,
                includeInactive: params.includeInactive,
                deleted: params.deleted,
            });

            const list = Array.isArray(response?.content)
                ? response.content
                : Array.isArray(response?.data)
                    ? response.data
                    : Array.isArray(response)
                        ? response
                        : [];

            const mappedProducts = list.map(product => {
                const currentStock = Number(product?.currentStock ?? product?.stock_actual ?? 0);
                const minStock = Number(product?.minStock ?? product?.min_stock ?? 0);
                const discount = Number(product?.discount ?? product?.descuento ?? 0);
                const categoryName = product?.category?.name ?? product?.category ?? '';
                const supplierName = product?.supplier?.name ?? product?.proveedor ?? '';

                return {
                    id: product.id,
                    name: product.name,
                    price: product.price,
                    descuento: discount,
                    discount,
                    stock_actual: currentStock,
                    currentStock,
                    min_stock: minStock,
                    minStock,
                    category: categoryName,
                    categoryName,
                    proveedor: supplierName,
                    supplierName,
                    photoUrl: product.photoUrl || '',
                    isAvailable: product.isAvailable,
                };
            });

            setProducts(mappedProducts);
            setPagination({
                currentPage: response?.number ?? params.page ?? 0,
                totalPages: response?.totalPages ?? 0,
                totalElements: response?.totalElements ?? mappedProducts.length,
                pageSize: response?.size ?? params.size ?? 10,
            });
        } catch (e) {
            console.error('Error fetching products:', e);
            setProducts([]);
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchProducts({ page: 0, size: 10 });
    }, [fetchProducts]);

    const addProduct = (product) => {
        setProducts(prev => [...prev, { ...product, id: prev.length + 1, category: initialCategories.find(c => c.id == product.category).name }]);
    };

    const updateProduct = (id, updatedProduct) => {
        setProducts(prev => prev.map(p => p.id === id ? { ...p, ...updatedProduct } : p));
    };

    const deleteProduct = (id) => {
        setProducts(prev => prev.filter(p => p.id !== id));
    };

    const value = {
        products,
        isLoading,
        pagination,
        fetchProducts,
        addProduct,
        updateProduct,
        deleteProduct,
    };

    return (
        <ProductsContext.Provider value={value}>
            {children}
        </ProductsContext.Provider>
    );
}

export function useProducts() {
    const context = useContext(ProductsContext);
    if (context === undefined) {
        throw new Error('useProducts debe usarse dentro de un ProductsProvider');
    }
    return context;
}