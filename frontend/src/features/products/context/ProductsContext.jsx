import initialCategories from '@/features/categories/mockedCategories';
import { createContext, useContext, useState, useEffect } from 'react';
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

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await getProducts();
                const list = Array.isArray(response?.data)
                    ? response.data
                    : Array.isArray(response?.content)
                        ? response.content
                        : Array.isArray(response)
                            ? response
                            : [];

                setProducts(list.map(product => ({
                    id: product.id,
                    name: product.name,
                    price: product.price,
                    descuento: product.discount || 0,
                    stock_actual: product.currentStock,
                    min_stock: product.minStock,
                    category: product.category?.name || '',
                    proveedor: product.supplier?.name || '',
                    photoUrl: product.photoUrl || '',
                    isAvailable: product.isAvailable,
                })));
            } catch (e) {
                console.error('Error fetching products:', e);
            }
        }
        fetchData();
    }, []);

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