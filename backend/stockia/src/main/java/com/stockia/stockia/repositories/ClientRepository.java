package com.stockia.stockia.repositories;

/*
 * Repositorio JPA para la entidad Cliente.
 * 
 * Métodos getter implementados para consultas específicas:
 * - Búsqueda por email individual
 * - Búsqueda por teléfono individual  
 * - Filtrado de clientes frecuentes
 * - Búsqueda combinada email OR teléfono (existente)
*/
import com.stockia.stockia.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /*
     * Busca cliente por email O teléfono (método original).
     * 
     * @param correoElectronico Email a buscar
     * 
     * @param telefono Teléfono a buscar
     * 
     * @return Optional con cliente si existe
     */

    Optional<Client> findByEmailOrPhone(String email, String phone);

    /*
     * GETTER: Busca cliente únicamente por email.
     * 
     * Método de consulta JPA que permite búsqueda específica por correo
     * electrónico.
     * Útil para validaciones de login o búsquedas directas.
     * 
     * Query SQL generado:
     * SELECT * FROM clientes WHERE correo_electronico = ?
     * 
     */

    Optional<Client> findByEmail(String email);

    /*
     * GETTER: Busca cliente únicamente por teléfono.
     * 
     * Método de consulta JPA para búsqueda específica por número telefónico.
     * Útil para validaciones de contacto o búsquedas por teléfono.
     * 
     * Query SQL generado:
     * SELECT * FROM clientes WHERE telefono = ?
     * 
     * @param telefono Número telefónico exacto (con código país)
     * 
     * @return Optional<Cliente> vacío si no existe, con datos si existe
     */

    Optional<Client> findByPhone(String phone);

    /**
     * GETTER: Obtiene todos los clientes frecuentes.
     * 
     * Método de consulta JPA que filtra clientes marcados como frecuentes.
     * Útil para reportes, promociones especiales, o análisis de clientes VIP.
     * 
     * Query SQL generado:
     * SELECT * FROM clientes WHERE cliente_frecuente = true
     * 
     * @return List<Cliente> lista de clientes frecuentes (puede estar vacía)
     */
    List<Client> findByIsFrequentTrue();

    Optional<Client> findById(UUID id);
}
