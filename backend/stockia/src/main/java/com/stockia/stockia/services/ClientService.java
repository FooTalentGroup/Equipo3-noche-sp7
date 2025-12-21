package com.stockia.stockia.services;

import com.stockia.stockia.dtos.client.ClientSearchRequestDto;
import com.stockia.stockia.enums.ClientStatus;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.exceptions.client.ClientProtectedException;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.exceptions.client.ClientDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.stockia.stockia.repositories.OrderRepository;
import com.stockia.stockia.mappers.OrderMapper;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.models.Order;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired 
    private OrderMapper orderMapper;

    private static final String CONSUMIDOR_FINAL_EMAIL = "consumidor-final@stockia.com";

    public Client registerClient(Client newClient) {
        Optional<Client> ExistentClient = clientRepository.findByEmailOrPhone(
                newClient.getEmail(),
                newClient.getPhone());

        if (ExistentClient.isPresent()) {
            throw new ClientDuplicatedException("El cliente ya está registrado con ese correo o teléfono.");
        }

        return clientRepository.save(newClient);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    public List<Client> getAllFrequentClients() {
        return clientRepository.findByIsFrequentTrue();
    }

    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


    public Optional<Client> findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    public Page<Client> searchClients(ClientSearchRequestDto params, Pageable pageable) {
        return clientRepository.searchClients(
                params.name(),
                params.email(),
                params.phone(),
                params.isFrequent(),
                params.clientStatus(),
                pageable);
    }

    public Client updateClient(UUID id, Client updatedData) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new com.stockia.stockia.exceptions.client.ClientNotFoundException(id));

        validateClientIsEditable(existingClient);

        if (!existingClient.getEmail().equals(updatedData.getEmail())) {
            Optional<Client> clientWithEmail = clientRepository.findByEmailAndIdNot(updatedData.getEmail(), id);
            if (clientWithEmail.isPresent()) {
                throw new ClientDuplicatedException("El email ya está asociado a otro cliente");
            }
        }

        if (!existingClient.getPhone().equals(updatedData.getPhone())) {
            Optional<Client> clientWithPhone = clientRepository.findByPhoneAndIdNot(updatedData.getPhone(), id);
            if (clientWithPhone.isPresent()) {
                throw new ClientDuplicatedException("El teléfono ya está asociado a otro cliente");
            }
        }

        existingClient.setName(updatedData.getName());
        existingClient.setEmail(updatedData.getEmail());
        existingClient.setPhone(updatedData.getPhone());
        existingClient.setIsFrequent(updatedData.getIsFrequent());

        return clientRepository.save(existingClient);
    }

    public void deleteClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        validateClientIsEditable(client);
        client.setClientStatus(ClientStatus.INACTIVE);
        clientRepository.save(client);
    }

    public List<OrderResponseDto> getClientPurchaseHistory(UUID clientId) {
    if (clientId == null) {
        throw new IllegalArgumentException("El ID del cliente no puede ser null");
    }
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new com.stockia.stockia.exceptions.client.ClientNotFoundException(clientId));
        
        List<Order> orders = orderRepository.findByCustomer_IdOrderByOrderDateDesc(clientId);
        
        return orders.stream()
            .map(orderMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public Client getFinalClient() {
        return clientRepository.findByEmail(CONSUMIDOR_FINAL_EMAIL)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Cliente Consumidor Final no encontrado"
                        ));
    }

    private void validateClientIsEditable(Client client) {
        if (client.getEmail().equalsIgnoreCase(CONSUMIDOR_FINAL_EMAIL)) {
            throw new ClientProtectedException(
                    "No se puede modificar ni dar de baja el cliente Consumidor Final"
            );
        }
    }
}