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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Búsqueda de clientes con filtros múltiples y paginación.
     *
     * Permite filtrar por nombre (parcial), email (exacto), teléfono (exacto) y si
     * es frecuente.
     * Todos los parámetros son opcionales (null = no filtrar por ese campo).
     *
     * @param name       Nombre del cliente (búsqueda parcial, case-insensitive)
     * @param email      Email del cliente (búsqueda exacta)
     * @param phone      Teléfono del cliente (búsqueda exacta)
     * @param isFrequent true para frecuentes, false para no frecuentes, null para
     *                   todos
     * @param pageable   Configuración de paginación y ordenamiento
     * @return Page con los clientes que cumplen los criterios
     */
    @Query("SELECT c FROM Client c WHERE " +
            "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR c.email = :email) AND " +
            "(:phone IS NULL OR c.phone = :phone) AND " +
            "(:isFrequent IS NULL OR c.isFrequent = :isFrequent)")
    Page<Client> searchClients(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("isFrequent") Boolean isFrequent,
            Pageable pageable);

    /**
     * Busca un cliente por email excluyendo un ID específico.
     * Útil para validar unicidad de email al actualizar un cliente.
     *
     * @param email Email a buscar
     * @param id    ID del cliente a excluir de la búsqueda
     * @return Optional con el cliente si existe otro con ese email
     */
    Optional<Client> findByEmailAndIdNot(String email, UUID id);

    /**
     * Busca un cliente por teléfono excluyendo un ID específico.
     * Útil para validar unicidad de teléfono al actualizar un cliente.
     *
     * @param phone Teléfono a buscar
     * @param id    ID del cliente a excluir de la búsqueda
     * @return Optional con el cliente si existe otro con ese teléfono
     */
    Optional<Client> findByPhoneAndIdNot(String phone, UUID id);
}
