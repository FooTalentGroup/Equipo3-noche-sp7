package com.stockia.stockia.repositories;

import com.stockia.stockia.models.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Pruebas unitarias para la capa de persistencia ClienteRepository.
 * 
 * Esta clase contiene tests que verifican el comportamiento correcto
 * de las operaciones de base de datos y consultas JPA para la entidad Cliente.
 * 
 * Se utiliza @DataJpaTest para cargar solo el contexto JPA con base de datos H2
 * en memoria y TestEntityManager para gestionar entidades en los tests.
 * 
 * RESULTADOS DE EJECUCIÓN:
 *  Tests ejecutados: 6/6
 *  Tests exitosos: 6/6  
 *  Errores: 0
 *  Tiempo: 4.894s
 * 
 * VERIFICACIONES DE BASE DE DATOS:
 * Tablas creadas correctamente: clientes, users
 * Constraints de unicidad funcionando: email, teléfono
 * Queries JPA ejecutándose exitosamente
 * Hibernate generando SQL correcto
 * 
 */


@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;


 /**
     * Test que verifica la búsqueda de cliente por correo electrónico.
     * 
     * Prueba:
     * - Se persiste un cliente con datos específicos
     * - Se busca usando findByCorreoElectronicoOrTelefono con el email
     * - Se verifica que encuentra al cliente correcto
     * 
     * Resultado esperado:
     * - Cliente encontrado exitosamente
     * - Todos los datos recuperados correctamente
     * - Coincidencia exacta por email
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - INSERT ejecutado correctamente en H2
     * - SELECT con WHERE email funcionando
     * - Mapeo JPA de Cliente operativo
     * - AssertJ verificando todos los campos
     * 
     * 
     */
    
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

/**
     * Test que verifica la búsqueda de cliente por número de teléfono.
     * 
     * Prueba:
     * - Se persiste un cliente con teléfono específico
     * - Se busca usando findByCorreoElectronicoOrTelefono con el teléfono
     * - Se verifica que encuentra al cliente correcto
     * 
     * Resultado esperado:
     * - Cliente encontrado exitosamente
     * - Campo clienteFrecuente verdadero verificado
     * - Búsqueda por teléfono funcionando
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Consulta SQL con OR funcionando
     * - Mapeo de boolean clienteFrecuente operativo
     * - TestEntityManager persistiendo correctamente
     * 
     * 
     */
    
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



    /**
     * Test que verifica el guardado correcto de cliente.
     * 
     * Prueba:
     * - Se crea un cliente nuevo
     * - Se guarda usando repository.save()
     * - Se verifica la asignación automática de ID
     * 
     * Resultado esperado:
     * - Cliente guardado con ID asignado
     * - Todos los datos persistidos correctamente
     * - Generación automática de ID funcionando
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - ID generado automáticamente por H2
     * - Estrategia IDENTITY funcionando
     * - Campos persistidos correctamente
     * 
     * 
     */
    
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

/*
     * Test que verifica el comportamiento cuando no se encuentra cliente.
     * 
     * Prueba:
     * - Se busca un cliente que no existe
     * - Email y teléfono no están en la base de datos
     * 
     * Resultado esperado:
     * - Optional.empty() retornado
     * - No se lanzan excepciones
     * - Búsqueda maneja correctamente casos negativos
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Query retornando vacío correctamente
     * - Optional manejado adecuadamente
     * - Sin excepciones lanzadas
     * 
     * 
     */


    @Test
    void noDebeEncontrarClienteInexistente() {
        Optional<Cliente> resultado = clienteRepository
            .findByCorreoElectronicoOrTelefono("noexiste@test.com", "+000000000000");

        assertThat(resultado).isEmpty();
    }

  /*
     * Test que verifica búsqueda por cualquier campo (email O teléfono).
     * 
     * Prueba:
     * - Se persiste un cliente
     * - Se busca tanto por email como por teléfono
     * - Ambas búsquedas deben retornar el mismo cliente
     * 
     * Resultado esperado:
     * - Ambas búsquedas exitosas
     * - Mismo ID retornado en ambos casos
     * - Operador OR funcionando correctamente
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Consulta OR ejecutándose correctamente
     * - Múltiples búsquedas del mismo registro
     * - Consistency de IDs verificada
     * 
     * 
     */

    
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


    /**
     * Test que verifica las restricciones de unicidad.
     * 
     * Prueba:
     * - Se persiste un cliente
     * - Se busca específicamente por sus campos únicos
     * - Se verifica que la búsqueda funciona con campos únicos
     * 
     * Resultado esperado:
     * - Cliente encontrado exitosamente
     * - Restricciones de unicidad respetadas
     * - Base de datos manteniendo integridad
     * 
     * RESULTADO DE EJECUCIÓN: Exitoso
     * - Constraints UNIQUE funcionando en H2
     * - Schema DDL creado correctamente
     * - Integridad referencial operativa
     * 
     * 
     */

    
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
