package com.stockia.stockia.services;

/**
 * Servicio de lógica de negocio para Client.
 * 
 * Métodos getter implementados:
 * - Obtener todos los Clients
 * - Obtener Client por ID
 * - Obtener Clients frecuentes
 * - Buscar por email específico
 * - Buscar por teléfono específico
 * - Buscar por email OR teléfono
 * 
 * 
 */

import com.stockia.stockia.dtos.client.ClientSearchRequestDto;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.exceptions.client.ClientDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    /*
     * GETTER: Obtiene todos los Clients registrados.
     * 
     * Método que devuelve la lista completa de Clients en el sistema.
     * Útil para pantallas de administración, reportes generales, o listados.
     * 
     * @return List<Client> lista completa de Clients (puede estar vacía)
     * 
     * @throws RuntimeException si hay error en base de datos
     */

    public Client registerClient(Client newClient) {
        Optional<Client> ExistentClient = clientRepository.findByEmailOrPhone(
                newClient.getEmail(),
                newClient.getPhone());

        if (ExistentClient.isPresent()) {
            throw new ClientDuplicatedException("El cliente ya está registrado con ese correo o teléfono.");
        }

        return clientRepository.save(newClient);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    /*
     * GETTER: Obtiene un Client específico por su ID.
     * 
     * Método que busca un Client usando su identificador único.
     * Útil para pantallas de detalle, edición, o consultas específicas.
     * 
     * @param id Identificador único del Client
     * 
     * @return Optional<Client> vacío si no existe, con datos si existe
     * 
     * @throws RuntimeException si hay error en base de datos
     */

    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    public List<Client> getAllFrequentClients() {
        return clientRepository.findByIsFrequentTrue();
    }

    /**
     * GETTER: Busca Client por email específico.
     * 
     * Método que permite búsqueda exacta por correo electrónico.
     * Útil para funciones de login, verificación de existencia, o búsquedas.
     * 
     * @param email Correo electrónico exacto a buscar
     * @return Optional<Client> vacío si no existe, con datos si existe
     * @throws RuntimeException si hay error en base de datos
     */

    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    /*
     * GETTER: Busca Client por teléfono específico.
     * 
     * Método que permite búsqueda exacta por número telefónico.
     * Útil para verificaciones de contacto, búsquedas rápidas, o validaciones.
     * 
     * @param telefono Número telefónico exacto a buscar (formato: +XX...)
     * 
     * @return Optional<Client> vacío si no existe, con datos si existe
     * 
     * @throws RuntimeException si hay error en base de datos
     */
    public Optional<Client> findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    /**
     * Busca clientes con filtros múltiples y paginación.
     *
     * Permite buscar clientes filtrando por nombre (parcial), email (exacto),
     * teléfono (exacto) y si es cliente frecuente.
     * Todos los filtros son opcionales.
     *
     * @param params   DTO con los parámetros de búsqueda
     * @param pageable Configuración de paginación y ordenamiento
     * @return Page con los clientes que cumplen los criterios
     */
    public Page<Client> searchClients(ClientSearchRequestDto params, Pageable pageable) {
        return clientRepository.searchClients(
                params.name(),
                params.email(),
                params.phone(),
                params.isFrequent(),
                pageable);
    }

    /**
     * Actualiza un cliente existente.
     *
     * Valida que:
     * - El cliente existe
     * - Si se cambió el email, que no esté asociado a otro cliente
     * - Si se cambió el teléfono, que no esté asociado a otro cliente
     *
     * @param id          ID del cliente a actualizar
     * @param updatedData Datos actualizados del cliente
     * @return Cliente actualizado
     * @throws com.stockia.stockia.exceptions.client.ClientNotFoundException si el
     *                                                                       cliente
     *                                                                       no
     *                                                                       existe
     * @throws ClientDuplicatedException                                     si el
     *                                                                       email o
     *                                                                       teléfono
     *                                                                       ya
     *                                                                       están
     *                                                                       en uso
     *                                                                       por
     *                                                                       otro
     *                                                                       cliente
     *                                                                       por
     *                                                                       otro
     *                                                                       cliente
     */
    public Client updateClient(UUID id, Client updatedData) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new com.stockia.stockia.exceptions.client.ClientNotFoundException(id));

        if (!existingClient.getEmail().equals(updatedData.getEmail())) {
            Optional<Client> clientWithEmail = clientRepository.findByEmailAndIdNot(updatedData.getEmail(), id);
            if (clientWithEmail.isPresent()) {
                throw new ClientDuplicatedException("El email ya está asociado a otro cliente");
            }
        }

        if (!existingClient.getPhone().equals(updatedData.getPhone())) {
            Optional<Client> clientWithPhone = clientRepository.findByPhoneAndIdNot(updatedData.getPhone(), id);
            if (clientWithPhone.isPresent()) {
                throw new ClientDuplicatedException("El teléfono ya está asociado a otro cliente");
            }
        }

        existingClient.setName(updatedData.getName());
        existingClient.setEmail(updatedData.getEmail());
        existingClient.setPhone(updatedData.getPhone());
        existingClient.setIsFrequent(updatedData.getIsFrequent());

        return clientRepository.save(existingClient);
    }
}
