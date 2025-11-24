package com.stockia.stockia.controllers;

import com.stockia.stockia.dtos.ClientRequestDto;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.services.ClientService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "05 - Clientes", description = "Endpoints para la gestión de clientes")
public class ClientController {

    @Autowired
    private ClientService ClientService;

    @PostMapping
    public ResponseEntity<Client> registrarClient(@Valid @RequestBody ClientRequestDto ClientDto) {
        Client newClient = new Client();
        newClient.setName(ClientDto.getName());
        newClient.setEmail(ClientDto.getEmail());
        newClient.setPhone(ClientDto.getPhone());
        newClient.setIsFrequent(ClientDto.getIsFrequentClient());

        try {
            Client ClientRegistrado = ClientService.registerClient(newClient);
            return new ResponseEntity<>(ClientRegistrado, HttpStatus.CREATED);
        } catch (ClientService.ClientDuplicatedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> Clients = ClientService.getAllClients();
        return new ResponseEntity<>(Clients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable UUID id) {
        Optional<Client> Client = ClientService.getClientById(id);
        if (Client.isPresent()) {
            return new ResponseEntity<>(Client.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client no encontrado");
        }
    }

    @GetMapping("/frecuentes")
    public ResponseEntity<List<Client>> getAllFrequentClients() {
        List<Client> ClientsFrecuentes = ClientService.getAllFrequentClients();
        return new ResponseEntity<>(ClientsFrecuentes, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Client> findByEmail(@RequestParam String email) {
        Optional<Client> Client = ClientService.findByEmail(email);
        if (Client.isPresent()) {
            return new ResponseEntity<>(Client.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client no encontrado");
        }
    }

    @GetMapping("/buscar/telefono")
    public ResponseEntity<Client> findByPhone(@RequestParam String telefono) {
        Optional<Client> Client = ClientService.findByPhone(telefono);
        if (Client.isPresent()) {
            return new ResponseEntity<>(Client.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client no encontrado con ese teléfono");
        }
    }
}