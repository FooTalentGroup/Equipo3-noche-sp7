package com.stockia.stockia.repositories;

import com.stockia.stockia.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCorreoElectronicoOrTelefono(String correoElectronico, String telefono);
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
    Optional<Cliente> findByTelefono(String telefono); 
    List<Cliente> findByClienteFrecuenteTrue();
}