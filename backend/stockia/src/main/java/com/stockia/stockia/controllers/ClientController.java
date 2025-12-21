package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.client.*;
import com.stockia.stockia.dtos.client.ClientRequestDto;
import com.stockia.stockia.dtos.client.ClientSearchRequestDto;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.services.ClientService;
import com.stockia.stockia.utils.ApiResult;
import com.stockia.stockia.dtos.order.OrderResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.ADMIN_OR_MANAGER;

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
                .isFrequent(clientDto.getIsFrequent())
                .build();

        Client clientRegistrado = clientService.registerClient(newClient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(clientRegistrado, "Cliente registrado exitosamente"));
    }

    @GetAllClientsWithFiltersDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<ApiResult<?>> getAllClients(
            @org.springdoc.core.annotations.ParameterObject ClientSearchRequestDto searchParams,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {

        Page<Client> clientsPage = clientService.searchClients(searchParams, pageable);
        return ResponseEntity.ok(ApiResult.success(clientsPage, "Clientes obtenidos exitosamente"));
    }

    @UpdateClientEndpointDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<?>> updateClient(
            @PathVariable UUID id,
            @Valid @RequestBody ClientRequestDto clientDto) {

        Client updatedClient = Client.builder()
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .isFrequent(clientDto.getIsFrequent())
                .build();

        Client clientActualizado = clientService.updateClient(id, updatedClient);
        return ResponseEntity.ok(ApiResult.success(clientActualizado, "Cliente actualizado exitosamente"));
    }

    @GetClientByIdEndpointDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<?>> getClientById(@PathVariable UUID id) {
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return ResponseEntity.ok(ApiResult.success(client, "Cliente encontrado"));
    }


    @DeleteClientEndpointDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> deleteClient(@PathVariable UUID id) {
        clientService.deleteClientById(id);
        return ResponseEntity.ok(ApiResult.success("Cliente dado de baja correctamente"));
    }

    @GetMapping("/{id}/purchase-history")
    @GetClientPurchaseHistoryDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<?>> getClientPurchaseHistory(@PathVariable("id") UUID clientId) {
        List<OrderResponseDto> purchaseHistory = clientService.getClientPurchaseHistory(clientId);
        
        if (purchaseHistory.isEmpty()) {
            return ResponseEntity.ok(ApiResult.success(
                purchaseHistory,
                "El cliente no registra compras"
            ));
        }
        
        return ResponseEntity.ok(ApiResult.success(
            purchaseHistory,
            String.format("Se encontraron %d compra(s) para el cliente", purchaseHistory.size())
        ));
    }

    @GetFinalClientEndpointDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/final")
    public ResponseEntity<ApiResult<?>> getFinalClient() {
        Client client = clientService.getFinalClient();
        return ResponseEntity.ok(
                ApiResult.success(client, "Cliente Consumidor Final encontrado")
        );
    }
}