package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.client.GetAllClientsWithFiltersDoc;
import com.stockia.stockia.documentation.client.GetClientByIdEndpointDoc;
import com.stockia.stockia.documentation.client.RegisterClientEndpointDoc;
import com.stockia.stockia.dtos.ClientRequestDto;
import com.stockia.stockia.dtos.client.ClientSearchRequestDto;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.services.ClientService;
import com.stockia.stockia.utils.ApiResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.ADMIN_OR_MANAGER;

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
    @PreAuthorize(ADMIN_OR_MANAGER)
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
                .body(ApiResult.success(clientRegistrado, "Cliente registrado exitosamente"));
    }

    /**
     * Obtiene clientes con paginación y filtros múltiples.
     *
     * Permite filtrar por:
     * - name: Nombre del cliente (búsqueda parcial)
     * - email: Email del cliente (búsqueda exacta)
     * - phone: Teléfono del cliente (búsqueda exacta)
     * - isFrequent: true para frecuentes, false para no frecuentes, null para todos
     *
     * Ejemplos de uso:
     * - GET /api/clients?page=0&size=20
     * - GET /api/clients?name=Juan&page=0&size=10
     * - GET /api/clients?email=juan@example.com
     * - GET /api/clients?isFrequent=true&page=0&size=20
     * - GET /api/clients?phone=+1234567890
     */
    @GetAllClientsWithFiltersDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<ApiResult<?>> getAllClients(
            @org.springdoc.core.annotations.ParameterObject ClientSearchRequestDto searchParams,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {

        Page<Client> clientsPage = clientService.searchClients(searchParams, pageable);
        return ResponseEntity.ok(ApiResult.success(clientsPage, "Clientes obtenidos exitosamente"));
    }

    @GetClientByIdEndpointDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<?>> getClientById(@PathVariable UUID id) {
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return ResponseEntity.ok(ApiResult.success(client, "Cliente encontrado"));
    }
}