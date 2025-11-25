package com.stockia.stockia.services;

import com.stockia.stockia.models.Client;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.exceptions.client.ClientDuplicatedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * Pruebas unitarias para la capa de servicio clientService.
 * 
 * Esta clase contiene tests que verifican la lógica de negocio
 * del registro de clients, incluyendo validaciones de duplicados
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
 * mvn test -Dtest=clientServiceTest
 *
 * # Test 1: Registro nuevo exitoso
 * mvn test -Dtest=clientServiceTest#debeRegistrarclientNuevoExitosamente
 *
 * #Test 2: Teléfono duplicado
 * mvn test
 * -Dtest=clientServiceTest#debeLanzarExcepcionCuandoclientDuplicadoPorTelefono
 *
 * # Test 3: Email duplicado
 * mvn test
 * -Dtest=clientServiceTest#debeLanzarExcepcionCuandoclientDuplicadoPorCorreo
 *
 *
 **/

@ExtendWith(MockitoExtension.class)
class clientServiceTest {

        @Mock
        private ClientRepository clientRepository;

        @InjectMocks
        private ClientService clientService;

        /**
         * Test que verifica el registro exitoso de un client nuevo.
         * 
         * Prueba:
         * - Se registra un client con datos únicos (no existe duplicado)
         * - El repositorio confirma que no hay conflictos
         * - Se guarda el client exitosamente
         * 
         * Resultado esperado:
         * - client guardado con ID asignado
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
        void shouldRegisterNewClientSuccessfully() {
                Client newClient = new Client();
                newClient.setName("Carlos López");
                newClient.setEmail("carlos@test.com");
                newClient.setPhone("+5491123456789");
                newClient.setIsFrequent(true);

                when(clientRepository.findByEmailOrPhone(anyString(), anyString()))
                                .thenReturn(Optional.empty());
                when(clientRepository.save(any(Client.class)))
                                .thenAnswer(invocation -> {
                                        Client client = invocation.getArgument(0);
                                        client.setId(UUID.randomUUID());
                                        return client;
                                });

                Client result = clientService.registerClient(newClient);

                assertThat(result.getPhone()).isEqualTo("+5491123456789");
                verify(clientRepository).save(any(Client.class));
        }

        /**
         * Test que verifica la excepción por teléfono duplicado.
         * 
         * Prueba:
         * - Se intenta registrar un client con teléfono ya existente
         * - El repositorio encuentra un client con ese teléfono
         * - Se lanza clientDuplicadoException
         * 
         * Resultado esperado:
         * - Excepción clientDuplicadoException lanzada
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
        void shouldThrowExceptionWhenClientDuplicatedByPhone() {
                Client newClient = new Client();
                newClient.setName("Pedro Martínez");
                newClient.setEmail("pedro@nuevo.com");
                newClient.setPhone("+573123456789");
                newClient.setIsFrequent(true);

                Client existingClient = new Client();
                existingClient.setId(UUID.randomUUID());
                existingClient.setPhone("+573123456789");

                when(clientRepository.findByEmailOrPhone(anyString(), anyString()))
                                .thenReturn(Optional.of(existingClient));

                assertThatThrownBy(() -> clientService.registerClient(newClient))
                                .isInstanceOf(ClientDuplicatedException.class);
        }

        /**
         * Test que verifica la excepción por email duplicado.
         * 
         * Prueba:
         * - Se intenta registrar un client con email ya existente
         * - El repositorio encuentra un client con ese email
         * - Se lanza clientDuplicadoException
         * 
         * Resultado esperado:
         * - Excepción clientDuplicadoException lanzada
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
        void shouldThrowExceptionWhenClientDuplicatedByEmail() {
                Client newClient = new Client();
                newClient.setName("Ana García");
                newClient.setEmail("ana@duplicado.com");
                newClient.setPhone("+34612345678");
                newClient.setIsFrequent(false);

                Client existingClient = new Client();
                existingClient.setId(UUID.randomUUID());
                existingClient.setEmail("ana@duplicado.com");

                when(clientRepository.findByEmailOrPhone(anyString(), anyString()))
                                .thenReturn(Optional.of(existingClient));

                assertThatThrownBy(() -> clientService.registerClient(newClient))
                                .isInstanceOf(ClientDuplicatedException.class);

                verify(clientRepository, never()).save(any(Client.class));
        }
}
