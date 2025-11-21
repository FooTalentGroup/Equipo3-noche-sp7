package com.stockia.stockia.controllers;

import com.stockia.stockia.dtos.ClienteRequestDto;
import com.stockia.stockia.models.Cliente;
import com.stockia.stockia.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> registrarCliente(@Valid @RequestBody ClienteRequestDto clienteDto) {

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(clienteDto.getNombre());
        nuevoCliente.setCorreoElectronico(clienteDto.getCorreoElectronico());
        nuevoCliente.setTelefono(clienteDto.getTelefono());
        nuevoCliente.setClienteFrecuente(clienteDto.getClienteFrecuente());

        try {
            Cliente clienteRegistrado = clienteService.registrarCliente(nuevoCliente);

            return new ResponseEntity<>(clienteRegistrado, HttpStatus.CREATED);

        } catch (ClienteService.ClienteDuplicadoException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
