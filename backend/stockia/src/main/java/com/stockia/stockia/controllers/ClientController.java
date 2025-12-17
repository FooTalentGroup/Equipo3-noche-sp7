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

/**
 * Controlador REST para la gestión de clientes.
 * Expone endpoints para el registro, búsqueda, consulta y actualización de
 * clientes.
 */


/**
 * Consulta el historial de compras completo de un cliente específico.
 * 
 * Este endpoint permite obtener todas las órdenes/compras realizadas por un cliente,
 * ordenadas cronológicamente de más reciente a más antigua. Requiere permisos
 * administrativos (ADMIN o MANAGER).
 * 
 * Casos de uso:
 * - Consulta de historial para atención al cliente
 * - Análisis de comportamiento de compra del cliente
 * - Reportes de ventas por cliente
 * - Validación de compras previas
 * 
 * @param clientId ID único del cliente (UUID)
 * @return ResponseEntity con ApiResult conteniendo:
 *         - Lista de OrderResponseDto si hay compras
 *         - Lista vacía si el cliente no tiene compras
 * @throws ClientNotFoundException si el cliente no existe en el sistema
 * @throws AccessDeniedException si el usuario no tiene permisos ADMIN/MANAGER
 * 
 * @apiNote Endpoint: GET /api/clients/{id}/purchase-history
 * @apiNote Requiere autenticación y rol ADMIN o MANAGER
 * @apiNote Respuesta 200: Historial obtenido exitosamente
 * @apiNote Respuesta 404: Cliente no encontrado
 * @apiNote Respuesta 401: Usuario no autenticado
 * @apiNote Respuesta 403: Sin permisos suficientes
 * 
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
                .isFrequent(clientDto.getIsFrequent())
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

    /**
     * Consulta el historial de compras de un cliente específico.
     * 
     * @param clientId ID del cliente
     * @return Lista de órdenes/compras del cliente ordenadas por fecha descendente
     */
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