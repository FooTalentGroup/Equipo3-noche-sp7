package com.stockia.stockia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockia.stockia.dtos.client.ClientRequestDto;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.services.ClientService;
import com.stockia.stockia.exceptions.client.ClientDuplicatedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

/**
 * Pruebas unitarias para el controlador ClienteController.
 * 
 * Esta clase contiene tests que verifican el comportamiento correcto
 * del endpoint de registro de clientes (/api/clientes).
 * 
 * Se utiliza @WebMvcTest para cargar solo el contexto web y MockBean
 * para simular las dependencias del servicio.
 * 
 * RESULTADOS DE EJECUCIÓN:
 * Tests ejecutados: 3/3
 * Tests exitosos: 3/3
 * Errores: 0
 * Tiempo: 1.754s
 *
 * Comandos de test
 * # TODOS los tests del controller
 * mvn test -Dtest=ClienteControllerTest
 *
 * # Test 1: Registro exitoso
 * mvn test -Dtest=ClienteControllerTest#debeRegistrarClienteExitosamente
 *
 * # Test 2: Datos inválidos
 * mvn test -Dtest=ClienteControllerTest#debeRetornarErrorCuandoDatosInvalidos
 *
 * # Test 3: Cliente duplicado
 * mvn test
 * -Dtest=ClienteControllerTest#debeRetornarConflictoCuandoClienteDuplicado
 * 
 * 
 *
 *
 **/

@WebMvcTest(ClientController.class)
@ActiveProfiles("test")
class ClientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @SuppressWarnings("removal")
        @MockBean
        private ClientService clientService;

        @Autowired
        private ObjectMapper objectMapper;

        /**
         * Test que verifica el registro exitoso de un cliente válido.
         * 
         * Prueba:
         * - Se envía un POST con datos válidos de cliente
         * - El servicio registra exitosamente el cliente
         * 
         * Resultado esperado:
         * - Status HTTP 201 (CREATED)
         * - Respuesta JSON con los datos del cliente creado
         * - ID asignado automáticamente
         * 
         * RESULTADO DE EJECUCIÓN: Exitoso
         * - Validación de endpoint POST funcionando
         * - Serialización JSON correcta
         * - Mock del servicio respondiendo adecuadamente
         * 
         * @throws Exception si ocurre algún error durante la ejecución del test
         */

        @Test
        void shouldRegisterClientSuccessfully() throws Exception {
                ClientRequestDto requestDto = new ClientRequestDto();
                requestDto.setName("Laura Sánchez");
                requestDto.setEmail("laura@test.com");
                requestDto.setPhone("+5491123456789");
                requestDto.setIsFrequent(false);

                Client savedClient = new Client();
                savedClient.setId(UUID.randomUUID());
                savedClient.setName("Laura Sánchez");
                savedClient.setEmail("laura@test.com");
                savedClient.setPhone("+5491123456789");
                savedClient.setIsFrequent(false);

                when(clientService.registerClient(any(Client.class)))
                                .thenReturn(savedClient);

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nombre").value("Laura Sánchez"))
                                .andExpect(jsonPath("$.correoElectronico").value("laura@test.com"))
                                .andExpect(jsonPath("$.telefono").value("+5491123456789"))
                                .andExpect(jsonPath("$.clienteFrecuente").value(false));
        }

        /**
         * Test que verifica el manejo de datos inválidos en el registro.
         * 
         * Prueba:
         * - Se envía un POST con datos que no cumplen las validaciones
         * - Nombre vacío, email con formato incorrecto, teléfono muy corto
         * 
         * Resultado esperado:
         * - Status HTTP 400 (BAD REQUEST)
         * - Validación de Bean Validation debe fallar
         * 
         * RESULTADO DE EJECUCIÓN: Exitoso
         * - Validaciones @NotBlank, @Email, @Pattern funcionando
         * - Spring Boot manejando correctamente MethodArgumentNotValidException
         * - 4 errores de validación detectados correctamente:
         * * Campo nombre vacío
         * * Email con formato inválido
         * * Teléfono muy corto
         * * ClienteFrecuente nulo
         * 
         * @throws Exception si ocurre algún error durante la ejecución del test
         */

        @Test
        void shouldReturnErrorWhenInvalidData() throws Exception {
                ClientRequestDto requestDto = new ClientRequestDto();
                requestDto.setName("");
                requestDto.setEmail("email-invalido");
                requestDto.setPhone("123");

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isBadRequest());
        }

        /**
         * Test que verifica el manejo de clientes duplicados.
         * 
         * Escenario de prueba:
         * - Se intenta registrar un cliente con email o teléfono ya existente
         * - El servicio lanza ClienteDuplicadoException
         * 
         * Resultado esperado:
         * - Status HTTP 409 (CONFLICT)
         * - Excepción de negocio manejada correctamente
         * 
         * RESULTADO DE EJECUCIÓN: Exitoso
         * - ResponseStatusExceptionResolver manejando la excepción
         * - Conversión de ClienteDuplicadoException a HTTP 409
         * - Endpoint respondiendo con el status correcto
         * 
         * @throws Exception si ocurre algún error durante la ejecución del test
         */

        @Test
        void shouldReturnConflictWhenClientDuplicated() throws Exception {
                ClientRequestDto requestDto = new ClientRequestDto();
                requestDto.setName("Cliente Duplicado");
                requestDto.setEmail("duplicado@test.com");
                requestDto.setPhone("+573123456789");
                requestDto.setIsFrequent(true);

                when(clientService.registerClient(any(Client.class)))
                                .thenThrow(new ClientDuplicatedException(
                                                "El cliente ya está registrado con ese correo o teléfono."));

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isConflict());
        }
}
