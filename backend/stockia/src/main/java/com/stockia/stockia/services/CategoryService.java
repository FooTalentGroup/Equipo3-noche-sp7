package com.stockia.stockia.services;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.category.CategorySearchRequestDto;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

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
     * Busca categorías con filtros dinámicos (nombre, estado activo, eliminadas).
     * Soporta paginación y ordenamiento.
     *
     * @param params Parámetros de búsqueda (nombre, isActive, deleted)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de categorías que cumplen con los criterios
     */
    Page<CategoryResponseDto> searchCategories(CategorySearchRequestDto params, Pageable pageable);

    /**
     * Actualiza una categoría existente.
     *
     * @param id  ID de la categoría a actualizar
     * @param dto Nuevos datos de la categoría
     * @return DTO con la categoría actualizada
     * @throws CategoryNotFoundException  si no se encuentra la categoría
     * @throws DuplicateCategoryException si el nuevo nombre ya existe
     */
    CategoryResponseDto updateCategory(UUID id, CategoryRequestDto dto);

    /**
     * Desactiva una categoría (soft delete).
     * No elimina físicamente, solo marca como inactiva.
     *
     * @param id ID de la categoría a desactivar
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    void deactivateCategory(UUID id);

    /**
     * Activa una categoría previamente desactivada.
     *
     * @param id ID de la categoría a activar
     * @return DTO con la categoría activada
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    CategoryResponseDto activateCategory(UUID id);

    /**
     * Elimina una categoría (soft delete).
     * Marca la categoría como eliminada sin borrarla físicamente.
     *
     * @param id ID de la categoría a eliminar
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    void deleteCategory(UUID id);

    /**
     * Restaura una categoría eliminada.
     *
     * @param id ID de la categoría a restaurar
     * @return DTO con la categoría restaurada
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    CategoryResponseDto restoreCategory(UUID id);

    /**
     * Elimina permanentemente una categoría de la base de datos.
     *
     * @param id ID de la categoría a eliminar permanentemente
     * @throws CategoryNotFoundException si no se encuentra la categoría
     */
    void permanentlyDeleteCategory(UUID id);
}
