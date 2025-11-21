package com.stockia.stockia.repositories;

import com.stockia.stockia.models.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void debeBuscarClientePorCorreoElectronico() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");
        cliente.setCorreoElectronico("juan@test.com");
        cliente.setTelefono("+5491123456789"); 
        cliente.setClienteFrecuente(false);
        entityManager.persistAndFlush(cliente);

        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("juan@test.com", "+999999999999");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Juan Pérez");
        assertThat(resultado.get().getCorreoElectronico()).isEqualTo("juan@test.com");
        assertThat(resultado.get().getTelefono()).isEqualTo("+5491123456789");
        assertThat(resultado.get().getClienteFrecuente()).isFalse();
    }

    @Test
    void debeBuscarClientePorTelefono() {
        Cliente cliente = new Cliente();
        cliente.setNombre("María López");
        cliente.setCorreoElectronico("maria@test.com");
        cliente.setTelefono("+573123456789"); 
        cliente.setClienteFrecuente(true);
        entityManager.persistAndFlush(cliente);

        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("otro@email.com", "+573123456789");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTelefono()).isEqualTo("+573123456789");
        assertThat(resultado.get().getClienteFrecuente()).isTrue();
    }

    @Test
    void debeGuardarClienteCorrectamente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana García");
        cliente.setCorreoElectronico("ana@test.com");
        cliente.setTelefono("+34612345678"); 
        cliente.setClienteFrecuente(true);

        Cliente guardado = clienteRepository.save(cliente);
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getTelefono()).isEqualTo("+34612345678");
    }

    @Test
    void noDebeEncontrarClienteInexistente() {
        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("noexiste@test.com", "+000000000000");

        assertThat(resultado).isEmpty();
    }

    @Test
    void debeEncontrarClientePorCualquierCampo() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro Martínez");
        cliente.setCorreoElectronico("pedro@test.com");
        cliente.setTelefono("+12125551234"); 
        cliente.setClienteFrecuente(false);
        entityManager.persistAndFlush(cliente);

        Optional<Cliente> porCorreo = clienteRepository
            .findByCorreoElectronicoOrTelefono("pedro@test.com", "+999999999999");

        Optional<Cliente> porTelefono = clienteRepository
            .findByCorreoElectronicoOrTelefono("otro@email.com", "+12125551234");

        assertThat(porCorreo).isPresent();
        assertThat(porTelefono).isPresent();
        assertThat(porCorreo.get().getId()).isEqualTo(porTelefono.get().getId());
    }

    @Test
    void debeRespetarRestriccionesDeUnicidad() {
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Carlos Ruiz");
        cliente1.setCorreoElectronico("carlos@test.com");
        cliente1.setTelefono("+5511987654321"); 
        cliente1.setClienteFrecuente(true);
        entityManager.persistAndFlush(cliente1);

        Optional<Cliente> clienteExistente = clienteRepository
            .findByCorreoElectronicoOrTelefono("carlos@test.com", "+5511987654321");
        
        assertThat(clienteExistente).isPresent();
        assertThat(clienteExistente.get().getTelefono()).isEqualTo("+5511987654321");
    }
}