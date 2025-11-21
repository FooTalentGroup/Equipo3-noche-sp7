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
        cliente.setTelefono("123456789");
        cliente.setClienteFrecuente(false);
        entityManager.persistAndFlush(cliente);

        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("juan@test.com", "999999999");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Juan Pérez");
        assertThat(resultado.get().getCorreoElectronico()).isEqualTo("juan@test.com");
        assertThat(resultado.get().getTelefono()).isEqualTo("123456789");
        assertThat(resultado.get().getClienteFrecuente()).isFalse();
    }

    @Test
    void debeBuscarClientePorTelefono() {
        Cliente cliente = new Cliente();
        cliente.setNombre("María López");
        cliente.setCorreoElectronico("maria@test.com");
        cliente.setTelefono("987654321");
        cliente.setClienteFrecuente(true);
        entityManager.persistAndFlush(cliente);

        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("otro@email.com", "987654321");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("María López");
        assertThat(resultado.get().getTelefono()).isEqualTo("987654321");
        assertThat(resultado.get().getClienteFrecuente()).isTrue();
    }

    @Test
    void debeGuardarClienteCorrectamente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana García");
        cliente.setCorreoElectronico("ana@test.com");
        cliente.setTelefono("555666777");
        cliente.setClienteFrecuente(true);

        Cliente guardado = clienteRepository.save(cliente);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getNombre()).isEqualTo("Ana García");
        assertThat(guardado.getCorreoElectronico()).isEqualTo("ana@test.com");
        assertThat(guardado.getTelefono()).isEqualTo("555666777");
        assertThat(guardado.getClienteFrecuente()).isTrue();
        
        Optional<Cliente> recuperado = clienteRepository.findById(guardado.getId());
        assertThat(recuperado).isPresent();
        assertThat(recuperado.get().getNombre()).isEqualTo("Ana García");
    }

    @Test
    void noDebeEncontrarClienteInexistente() {
        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("noexiste@test.com", "000000000");

        assertThat(resultado).isEmpty();
    }

    @Test
    void debeEncontrarClientePorCualquierCampo() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro Martínez");
        cliente.setCorreoElectronico("pedro@test.com");
        cliente.setTelefono("111222333");
        cliente.setClienteFrecuente(false);
        entityManager.persistAndFlush(cliente);

        Optional<Cliente> porCorreo = clienteRepository
            .findByCorreoElectronicoOrTelefono("pedro@test.com", "999999999");

        Optional<Cliente> porTelefono = clienteRepository
            .findByCorreoElectronicoOrTelefono("otro@email.com", "111222333");

        assertThat(porCorreo).isPresent();
        assertThat(porTelefono).isPresent();
        assertThat(porCorreo.get().getId()).isEqualTo(porTelefono.get().getId());
        assertThat(porCorreo.get().getNombre()).isEqualTo("Pedro Martínez");
        assertThat(porTelefono.get().getNombre()).isEqualTo("Pedro Martínez");
    }

    @Test
    void debeRespetarRestriccionesDeUnicidad() {
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Carlos Ruiz");
        cliente1.setCorreoElectronico("carlos@test.com");
        cliente1.setTelefono("444555666");
        cliente1.setClienteFrecuente(true);
        entityManager.persistAndFlush(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Otro Carlos");
        cliente2.setCorreoElectronico("carlos@test.com"); 
        cliente2.setTelefono("777888999"); 
        cliente2.setClienteFrecuente(false);

        Optional<Cliente> clienteExistente = clienteRepository
            .findByCorreoElectronicoOrTelefono("carlos@test.com", "444555666");
        assertThat(clienteExistente).isPresent();
        assertThat(clienteExistente.get().getNombre()).isEqualTo("Carlos Ruiz");
    }
}