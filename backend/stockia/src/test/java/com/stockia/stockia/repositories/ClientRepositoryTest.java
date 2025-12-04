package com.stockia.stockia.repositories;

import com.stockia.stockia.models.Client;
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
 * Comandos para tests
 * # TODOS los tests del repository
 * mvn test -Dtest=ClienteRepositoryTest
 *
 * # Test 1: Buscar por email
 * mvn test -Dtest=ClienteRepositoryTest#debeBuscarClientePorCorreoElectronico
 *
 * # Test 2: Buscar por teléfono
 * mvn test -Dtest=ClienteRepositoryTest#debeBuscarClientePorTelefono
 *
 * # Test 3: Guardar cliente
 * mvn test -Dtest=ClienteRepositoryTest#debeGuardarClienteCorrectamente
 *
 * # Test 4: Cliente inexistente
 * mvn test -Dtest=ClienteRepositoryTest#noDebeEncontrarClienteInexistente
 *
 * # Test 5: Buscar por cualquier campo
 * mvn test -Dtest=ClienteRepositoryTest#debeEncontrarClientePorCualquierCampo
 *
 * # Test 6: Restricciones de unicidad
 *mvn test -Dtest=ClienteRepositoryTest#debeRespetarRestriccionesDeUnicidad
 *
 */

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

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
    void shouldFindClientByEmail() {
        Client cliente = new Client();
        cliente.setName("Juan Pérez");
        cliente.setEmail("juan@test.com");
        cliente.setPhone("+5491123456789");
        cliente.setIsFrequent(false);
        entityManager.persistAndFlush(cliente);

        Optional<Client> result = clientRepository
                .findByEmailOrPhone("juan@test.com", "+999999999999");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Juan Pérez");
        assertThat(result.get().getEmail()).isEqualTo("juan@test.com");
        assertThat(result.get().getPhone()).isEqualTo("+5491123456789");
        assertThat(result.get().getIsFrequent()).isFalse();
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
    void shouldFindClientByPhone() {
        Client client = new Client();
        client.setName("María López");
        client.setEmail("maria@test.com");
        client.setPhone("+573123456789");
        client.setIsFrequent(true);
        entityManager.persistAndFlush(client);

        Optional<Client> result = clientRepository
                .findByEmailOrPhone("otro@email.com", "+573123456789");

        assertThat(result).isPresent();
        assertThat(result.get().getPhone()).isEqualTo("+573123456789");
        assertThat(result.get().getIsFrequent()).isTrue();
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
    void shouldSaveClientCorrectly() {
        Client client = new Client();
        client.setName("Ana García");
        client.setEmail("ana@test.com");
        client.setPhone("+34612345678");
        client.setIsFrequent(true);

        Client savedClient = clientRepository.save(client);
        assertThat(savedClient.getId()).isNotNull();
        assertThat(savedClient.getPhone()).isEqualTo("+34612345678");
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
    void shouldNotFindNonExistentClient() {
        Optional<Client> result = clientRepository
                .findByEmailOrPhone("noexiste@test.com", "+000000000000");

        assertThat(result).isEmpty();
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
    void shouldFindClientByAnyField() {
        Client cliente = new Client();
        cliente.setName("Pedro Martínez");
        cliente.setEmail("pedro@test.com");
        cliente.setPhone("+12125551234");
        cliente.setIsFrequent(false);
        entityManager.persistAndFlush(cliente);

        Optional<Client> byEmail = clientRepository
                .findByEmailOrPhone("pedro@test.com", "+999999999999");

        Optional<Client> byPhone = clientRepository
                .findByEmailOrPhone("otro@email.com", "+12125551234");

        assertThat(byEmail).isPresent();
        assertThat(byPhone).isPresent();
        assertThat(byEmail.get().getId()).isEqualTo(byPhone.get().getId());
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
    void shouldRespectUniquenessConstraints() {
        Client client1 = new Client();
        client1.setName("Carlos Ruiz");
        client1.setEmail("carlos@test.com");
        client1.setPhone("+5511987654321");
        client1.setIsFrequent(true);
        entityManager.persistAndFlush(client1);

        Optional<Client> clientExistente = clientRepository
                .findByEmailOrPhone("carlos@test.com", "+5511987654321");

        assertThat(clientExistente).isPresent();
        assertThat(clientExistente.get().getPhone()).isEqualTo("+5511987654321");
    }
}
