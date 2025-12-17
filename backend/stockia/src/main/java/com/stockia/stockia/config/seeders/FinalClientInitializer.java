package com.stockia.stockia.config.seeders;

import com.stockia.stockia.enums.ClientStatus;
import com.stockia.stockia.models.Client;
import com.stockia.stockia.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class FinalClientInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;

    @Override
    public void run(String... args) {

        String email = "consumidor-final@stockia.com";

        Optional<Client> aux = clientRepository.findByEmail(email);

        if (aux.isEmpty()) {
            Client consumidorFinal = Client.builder()
                    .name("Consumidor Final")
                    .email(email)
                    .phone("+0000000000")
                    .isFrequent(false)
                    .clientStatus(ClientStatus.ACTIVE)
                    .build();

            clientRepository.save(consumidorFinal);
            System.out.println("Cliente 'Consumidor Final' creado.");
            return;
        }

        Client client = aux.get();
        if (client.getClientStatus() != ClientStatus.ACTIVE) {
            client.setClientStatus(ClientStatus.ACTIVE);
            clientRepository.save(client);
            System.out.println("Cliente 'Consumidor Final' actualizado a ACTIVE.");
        }
    }
}
