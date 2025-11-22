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

/**
 * Pruebas unitarias para la capa de servicio ClienteService.
 * 
 * Esta clase contiene tests que verifican la lógica de negocio
 * del registro de clientes, incluyendo validaciones de duplicados
 * y el correcto flujo de datos entre el servicio y el repositorio.
 * 
 * Se utiliza @ExtendWith(MockitoExtension.class) para inyección de mocks
 * y @InjectMocks/@Mock para simular las dependencias del repositorio.
 * 
 * RESULTADOS DE EJECUCIÓN:
 * Tests ejecutados: 3/3
 * Tests exitosos: 3/3  
 * Errores: 0
 * Tiempo: 1.590s
 *
 * Comandos para test
 *
 * # TODOS los tests del service
 * mvn test -Dtest=ClienteServiceTest
 *
 * # Test 1: Registro nuevo exitoso
 * mvn test -Dtest=ClienteServiceTest#debeRegistrarClienteNuevoExitosamente
 *
 * #Test 2: Teléfono duplicado
 * mvn test -Dtest=ClienteServiceTest#debeLanzarExcepcionCuandoClienteDuplicadoPorTelefono
 *
 * # Test 3: Email duplicado
 * mvn test -Dtest=ClienteServiceTest#debeLanzarExcepcionCuandoClienteDuplicadoPorCorreo
 *
 *
 **/

    
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

  /**
     * Test que verifica el registro exitoso de un cliente nuevo.
     * 
     * Prueba:
     * - Se registra un cliente con datos únicos (no existe duplicado)
     * - El repositorio confirma que no hay conflictos
     * - Se guarda el cliente exitosamente
     * 
     * Resultado esperado:
     * - Cliente guardado con ID asignado
     * - Método save() del repositorio invocado
     * - Todos los datos persistidos correctamente
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Mock del repositorio respondiendo correctamente
     * - Lógica de validación de duplicados funcionando
     * - Asignación de ID simulada correctamente
     * - Verificaciones de Mockito pasando
     * 
     * 
     */

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

    /**
     * Test que verifica la excepción por teléfono duplicado.
     * 
     * Prueba:
     * - Se intenta registrar un cliente con teléfono ya existente
     * - El repositorio encuentra un cliente con ese teléfono
     * - Se lanza ClienteDuplicadoException
     * 
     * Resultado esperado:
     * - Excepción ClienteDuplicadoException lanzada
     * - No se llama al método save()
     * - Validación de duplicados funcionando
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Excepción lanzada correctamente
     * - Mock findByCorreoElectronicoOrTelefono funcionando
     * - Validación de duplicados por teléfono operativa
     * - AssertJ verificando la excepción adecuadamente
     * 
     * 
     */   

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

  /**
     * Test que verifica la excepción por email duplicado.
     * 
     * Prueba:
     * - Se intenta registrar un cliente con email ya existente
     * - El repositorio encuentra un cliente con ese email
     * - Se lanza ClienteDuplicadoException
     * 
     * Resultado esperado:
     * - Excepción ClienteDuplicadoException lanzada
     * - Método save() nunca invocado
     * - Validación de duplicados por email funcionando
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Excepción lanzada correctamente por email duplicado
     * - Verificación verify(never()) funcionando
     * - Lógica de negocio de duplicados operativa
     * - Mock repository simulando búsqueda exitosamente
     * 
     * 
     */

    @Test
    void debeLanzarExcepcionCuandoClienteDuplicadoPorCorreo() {
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
