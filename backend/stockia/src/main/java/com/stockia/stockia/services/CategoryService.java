package com.stockia.stockia.services;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de categorías de productos.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
public interface CategoryService {

    /**
     * Crea una nueva categoría.
     *
     * @param dto Datos de la categoría a crear
     * @return DTO con la categoría creada
     * @throws DuplicateCategoryException si ya existe una categoría con ese nombre
     */
    CategoryResponseDto createCategory(CategoryRequestDto dto);

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id ID de la categoría
     * @return DTO con la categoría encontrada
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    CategoryResponseDto getCategoryById(Long id);

    /**
     * Lista todas las categorías (activas e inactivas).
     *
     * @return Lista de todas las categorías
     */
    List<CategoryResponseDto> getAllCategories();

    /**
     * Lista solo las categorías activas.
     *
     * @return Lista de categorías activas
     */
    List<CategoryResponseDto> getActiveCategories();

    /**
     * Actualiza una categoría existente.
     *
     * @param id ID de la categoría a actualizar
     * @param dto Nuevos datos de la categoría
     * @return DTO con la categoría actualizada
     * @throws CategoryNotFoundException si no se encuentra la categoría
     * @throws DuplicateCategoryException si el nuevo nombre ya existe
     */
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto);

    /**
     * Desactiva una categoría (soft delete).
     * No elimina físicamente, solo marca como inactiva.
     *
     * @param id ID de la categoría a desactivar
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    void deactivateCategory(Long id);

    /**
     * Activa una categoría previamente desactivada.
     *
     * @param id ID de la categoría a activar
     * @return DTO con la categoría activada
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    CategoryResponseDto activateCategory(Long id);

    /**
     * Elimina una categoría (soft delete).
     * Marca la categoría como eliminada sin borrarla físicamente.
     *
     * @param id ID de la categoría a eliminar
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    void deleteCategory(Long id);

    /**
     * Lista las categorías eliminadas.
     *
     * @return Lista de categorías eliminadas
     */
    List<CategoryResponseDto> getDeletedCategories();

    /**
     * Restaura una categoría eliminada.
     *
     * @param id ID de la categoría a restaurar
     * @return DTO con la categoría restaurada
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    CategoryResponseDto restoreCategory(Long id);

    /**
     * Elimina permanentemente una categoría de la base de datos.
     *
     * @param id ID de la categoría a eliminar permanentemente
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    void permanentlyDeleteCategory(Long id);
}

