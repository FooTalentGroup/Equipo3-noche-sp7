package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.client.*;
import com.stockia.stockia.dtos.ClientRequestDto;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.services.ClientService;
import com.stockia.stockia.utils.ApiResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión de clientes.
 * Expone endpoints para el registro, búsqueda y consulta de clientes.
 */
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "05 - Clientes", description = "Endpoints para la gestión de clientes")
public class ClientController {

    private final ClientService clientService;

    @RegisterClientEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResult<?>> registrarClient(@Valid @RequestBody ClientRequestDto clientDto) {
        Client newClient = Client.builder()
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .isFrequent(clientDto.getIsFrequentClient())
                .build();

        Client clientRegistrado = clientService.registerClient(newClient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(clientRegistrado, "Cliente registrado exitosamente."));
    }

    @GetAllClientsEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ApiResult<?>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(ApiResult.success(clients, "Clientes obtenidos exitosamente."));
    }

    @GetClientByIdEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<?>> getClientById(@PathVariable UUID id) {
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return ResponseEntity.ok(ApiResult.success(client, "Cliente encontrado."));
    }

    @GetFrequentClientsEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/frecuentes")
    public ResponseEntity<ApiResult<?>> getAllFrequentClients() {
        List<Client> clientesFrecuentes = clientService.getAllFrequentClients();
        return ResponseEntity.ok(ApiResult.success(clientesFrecuentes, "Clientes frecuentes obtenidos exitosamente."));
    }

    @FindClientByEmailEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResult<?>> findByEmail(@RequestParam String email) {
        Client client = clientService.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado con email: " + email));
        return ResponseEntity.ok(ApiResult.success(client, "Cliente encontrado."));
    }

    @FindClientByPhoneEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buscar/telefono")
    public ResponseEntity<ApiResult<?>> findByPhone(@RequestParam String telefono) {
        Client client = clientService.findByPhone(telefono)
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado con teléfono: " + telefono));
        return ResponseEntity.ok(ApiResult.success(client, "Cliente encontrado."));
    }
}