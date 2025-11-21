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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeRegistrarClienteExitosamente() throws Exception {
        ClienteRequestDto requestDto = new ClienteRequestDto();
        requestDto.setNombre("Laura Sánchez");
        requestDto.setCorreoElectronico("laura@test.com");
        requestDto.setTelefono("1234567890");
        requestDto.setClienteFrecuente(false);

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Laura Sánchez");
        clienteGuardado.setCorreoElectronico("laura@test.com");
        clienteGuardado.setTelefono("1234567890");
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
                .andExpect(jsonPath("$.telefono").value("1234567890"))
                .andExpect(jsonPath("$.clienteFrecuente").value(false));
    }

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

    @Test
    void debeRetornarConflictoCuandoClienteDuplicado() throws Exception {
        ClienteRequestDto requestDto = new ClienteRequestDto();
        requestDto.setNombre("Cliente Duplicado");
        requestDto.setCorreoElectronico("duplicado@test.com");
        requestDto.setTelefono("9876543210");
        requestDto.setClienteFrecuente(true);

        when(clienteService.registrarCliente(any(Cliente.class)))
            .thenThrow(new ClienteDuplicadoException("El cliente ya está registrado con ese correo o teléfono."));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }
}