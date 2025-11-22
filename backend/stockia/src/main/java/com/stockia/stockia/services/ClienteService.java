package com.stockia.stockia.services;

/**
 * Servicio de lógica de negocio para Cliente.
 * 
 * Métodos getter implementados:
 * - Obtener todos los clientes
 * - Obtener cliente por ID
 * - Obtener clientes frecuentes
 * - Buscar por email específico
 * - Buscar por teléfono específico
 * - Buscar por email OR teléfono
 * 
 * 
 */


import com.stockia.stockia.models.Cliente;
import com.stockia.stockia.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /*
     *  GETTER: Obtiene todos los clientes registrados.
     * 
     * Método que devuelve la lista completa de clientes en el sistema.
     * Útil para pantallas de administración, reportes generales, o listados.
     * 
     * @return List<Cliente> lista completa de clientes (puede estar vacía)
     * @throws RuntimeException si hay error en base de datos
     */
    

    public static class ClienteDuplicadoException extends RuntimeException {
        public ClienteDuplicadoException(String mensaje) {
            super(mensaje);
        }
    }
    
    public Cliente registrarCliente(Cliente nuevoCliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findByCorreoElectronicoOrTelefono(
            nuevoCliente.getCorreoElectronico(), 
            nuevoCliente.getTelefono()
        );

        if (clienteExistente.isPresent()) {
            throw new ClienteDuplicadoException("El cliente ya está registrado con ese correo o teléfono.");
        }

        return clienteRepository.save(nuevoCliente);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    /*
     *  GETTER: Obtiene un cliente específico por su ID.
     * 
     * Método que busca un cliente usando su identificador único.
     * Útil para pantallas de detalle, edición, o consultas específicas.
     * 
     * @param id Identificador único del cliente
     * @return Optional<Cliente> vacío si no existe, con datos si existe
     * @throws RuntimeException si hay error en base de datos
     */
    
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }
    

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> obtenerClientesFrecuentes() {
        return clienteRepository.findByClienteFrecuenteTrue();
    }
     /**
     * GETTER: Busca cliente por email específico.
     * 
     * Método que permite búsqueda exacta por correo electrónico.
     * Útil para funciones de login, verificación de existencia, o búsquedas.
     * 
     * @param email Correo electrónico exacto a buscar
     * @return Optional<Cliente> vacío si no existe, con datos si existe
     * @throws RuntimeException si hay error en base de datos
     */

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByCorreoElectronico(email);
    }

    /*
     *  GETTER: Busca cliente por teléfono específico.
     * 
     * Método que permite búsqueda exacta por número telefónico.
     * Útil para verificaciones de contacto, búsquedas rápidas, o validaciones.
     * 
     * @param telefono Número telefónico exacto a buscar (formato: +XX...)
     * @return Optional<Cliente> vacío si no existe, con datos si existe
     * @throws RuntimeException si hay error en base de datos
     */
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return clienteRepository.findByTelefono(telefono);
    }
}
