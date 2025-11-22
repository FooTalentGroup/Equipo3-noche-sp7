package com.stockia.stockia.services;

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

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> obtenerClientesFrecuentes() {
        return clienteRepository.findByClienteFrecuenteTrue();
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByCorreoElectronico(email);
    }

    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return clienteRepository.findByTelefono(telefono);
    }
}