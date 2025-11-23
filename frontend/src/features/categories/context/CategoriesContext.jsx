import { createContext, useContext, useState } from 'react';
import initialCategories from '../mockedCategories';

const CategoriesContext = createContext(undefined);

export function CategoriesProvider({ children }) {
    const [categories, setCategories] = useState(initialCategories);

    const addCategory = (category) => {
        setCategories(prev => [...prev, { ...category, id: prev.length + 1 }]);
    };

    const updateCategory = (id, updatedCategory) => {
        setCategories(prev => prev.map(c => c.id === id ? { ...c, ...updatedCategory } : c));
    };

    const deleteCategory = (id) => {
        setCategories(prev => prev.filter(c => c.id !== id));
    };

    const getCategoryById = (id) => {
        return categories.find(c => c.id === id);
    };

    const getCategoryByName = (name) => {
        return categories.find(c => c.name.toLowerCase() === name.toLowerCase());
    };

    const value = {
        categories,
        addCategory,
        updateCategory,
        deleteCategory,
        getCategoryById,
        getCategoryByName,
    };

    return (
        <CategoriesContext.Provider value={value}>
            {children}
        </CategoriesContext.Provider>
    );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useCategories() {
    const context = useContext(CategoriesContext);
    if (context === undefined) {
        throw new Error('useCategories debe usarse dentro de un CategoriesProvider');
    }
    return context;
}