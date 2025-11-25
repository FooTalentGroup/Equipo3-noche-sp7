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

import com.stockia.stockia.models.Client;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.exceptions.client.ClientDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
