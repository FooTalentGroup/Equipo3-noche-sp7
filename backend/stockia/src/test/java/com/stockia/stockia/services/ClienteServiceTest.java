package com.stockia.stockia.services;

import com.stockia.stockia.models.Cliente;
import com.stockia.stockia.repositories.ClienteRepository;
import com.stockia.stockia.services.ClienteService.ClienteDuplicadoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void debeRegistrarClienteNuevoExitosamente() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Carlos López");
        nuevoCliente.setCorreoElectronico("carlos@test.com");
        nuevoCliente.setTelefono("+5491123456789"); 
        nuevoCliente.setClienteFrecuente(true);

        when(clienteRepository.findByCorreoElectronicoOrTelefono(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class)))
            .thenAnswer(invocation -> {
                Cliente cliente = invocation.getArgument(0);
                cliente.setId(1L);
                return cliente;
            });

        Cliente resultado = clienteService.registrarCliente(nuevoCliente);

        assertThat(resultado.getTelefono()).isEqualTo("+5491123456789");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void debeLanzarExcepcionCuandoClienteDuplicadoPorTelefono() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Pedro Martínez");
        nuevoCliente.setCorreoElectronico("pedro@nuevo.com");
        nuevoCliente.setTelefono("+573123456789"); 
        nuevoCliente.setClienteFrecuente(true);

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(2L);
        clienteExistente.setTelefono("+573123456789");

        when(clienteRepository.findByCorreoElectronicoOrTelefono(anyString(), anyString()))
            .thenReturn(Optional.of(clienteExistente));

        assertThatThrownBy(() -> clienteService.registrarCliente(nuevoCliente))
            .isInstanceOf(ClienteDuplicadoException.class);
    }

    @Test
    void debeLanzarExcepcionCuandoClienteDuplicadoPorCorreo() {
        // Given
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Ana García");
        nuevoCliente.setCorreoElectronico("ana@duplicado.com");
        nuevoCliente.setTelefono("+34612345678"); 
        nuevoCliente.setClienteFrecuente(false);

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setCorreoElectronico("ana@duplicado.com");

        when(clienteRepository.findByCorreoElectronicoOrTelefono(anyString(), anyString()))
            .thenReturn(Optional.of(clienteExistente));

        assertThatThrownBy(() -> clienteService.registrarCliente(nuevoCliente))
            .isInstanceOf(ClienteDuplicadoException.class);
        
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}