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

import java.util.List;
import java.util.Optional;

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


    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
    }

    @GetMapping("/frecuentes")
    public ResponseEntity<List<Cliente>> obtenerClientesFrecuentes() {
        List<Cliente> clientesFrecuentes = clienteService.obtenerClientesFrecuentes();
        return new ResponseEntity<>(clientesFrecuentes, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Cliente> buscarPorEmail(@RequestParam String email) {
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
    }

    @GetMapping("/buscar/telefono")
    public ResponseEntity<Cliente> buscarPorTelefono(@RequestParam String telefono) {
        Optional<Cliente> cliente = clienteService.buscarPorTelefono(telefono);
        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ese teléfono");
        }
    }
}