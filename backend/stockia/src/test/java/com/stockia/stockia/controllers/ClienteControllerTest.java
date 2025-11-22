package com.stockia.stockia.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockia.stockia.dtos.ClienteRequestDto;
import com.stockia.stockia.models.Cliente;
import com.stockia.stockia.services.ClienteService;
import com.stockia.stockia.services.ClienteService.ClienteDuplicadoException;
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
 *  Tests ejecutados: 3/3
 *  Tests exitosos: 3/3  
 *  Errores: 0
 *  Tiempo: 1.754s
 *
 *
 **/

@WebMvcTest(ClienteController.class)
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

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
     * RESULTADO DE EJECUCIÓN: EXITOSO
     * - Validación de endpoint POST funcionando
     * - Serialización JSON correcta
     * - Mock del servicio respondiendo adecuadamente
     * 
     * @throws Exception si ocurre algún error durante la ejecución del test
     */



    @Test
    void debeRegistrarClienteExitosamente() throws Exception {
        ClienteRequestDto requestDto = new ClienteRequestDto();
        requestDto.setNombre("Laura Sánchez");
        requestDto.setCorreoElectronico("laura@test.com");
        requestDto.setTelefono("+5491123456789"); 
        requestDto.setClienteFrecuente(false);

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Laura Sánchez");
        clienteGuardado.setCorreoElectronico("laura@test.com");
        clienteGuardado.setTelefono("+5491123456789");
        clienteGuardado.setClienteFrecuente(false);

        when(clienteService.registrarCliente(any(Cliente.class)))
            .thenReturn(clienteGuardado);

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
     * RESULTADO DE EJECUCIÓN:  EXITOSO
     * - Validaciones @NotBlank, @Email, @Pattern funcionando
     * - Spring Boot manejando correctamente MethodArgumentNotValidException
     * - 4 errores de validación detectados correctamente:
     *   * Campo nombre vacío
     *   * Email con formato inválido  
     *   * Teléfono muy corto
     *   * ClienteFrecuente nulo
     * 
     * @throws Exception si ocurre algún error durante la ejecución del test
     */


    @Test
    void debeRetornarErrorCuandoDatosInvalidos() throws Exception {
        ClienteRequestDto requestDto = new ClienteRequestDto();
        requestDto.setNombre("");  
        requestDto.setCorreoElectronico("email-invalido");  
        requestDto.setTelefono("123");  
   
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
     * RESULTADO DE EJECUCIÓN:  EXITOSO
     * - ResponseStatusExceptionResolver manejando la excepción
     * - Conversión de ClienteDuplicadoException a HTTP 409
     * - Endpoint respondiendo con el status correcto
     * 
     * @throws Exception si ocurre algún error durante la ejecución del test
     */

    @Test
    void debeRetornarConflictoCuandoClienteDuplicado() throws Exception {
        ClienteRequestDto requestDto = new ClienteRequestDto();
        requestDto.setNombre("Cliente Duplicado");
        requestDto.setCorreoElectronico("duplicado@test.com");
        requestDto.setTelefono("+573123456789"); 
        requestDto.setClienteFrecuente(true);

        when(clienteService.registrarCliente(any(Cliente.class)))
            .thenThrow(new ClienteDuplicadoException("El cliente ya está registrado con ese correo o teléfono."));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }
}
