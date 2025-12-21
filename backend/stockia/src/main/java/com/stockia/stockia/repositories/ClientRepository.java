package com.stockia.stockia.repositories;

import com.stockia.stockia.enums.ClientStatus;
import com.stockia.stockia.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

        Optional<Client> findByEmailOrPhone(String email, String phone);


        Optional<Client> findByEmail(String email);

        Optional<Client> findByPhone(String phone);

        List<Client> findByIsFrequentTrue();

        Optional<Client> findById(UUID id);

        @Query("SELECT c FROM Client c WHERE " +
                        "(:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                        "(:email IS NULL OR :email = '' OR c.email = :email) AND " +
                        "(:phone IS NULL OR :phone = '' OR c.phone = :phone) AND " +
                        "(:isFrequent IS NULL OR c.isFrequent = :isFrequent) AND " +
                        "(:clientStatus IS NULL OR c.clientStatus = :clientStatus)")
        Page<Client> searchClients(
                        @Param("name") String name,
                        @Param("email") String email,
                        @Param("phone") String phone,
                        @Param("isFrequent") Boolean isFrequent,
                        @Param("clientStatus") ClientStatus clientStatus,
                        Pageable pageable);

        Optional<Client> findByEmailAndIdNot(String email, UUID id);

        Optional<Client> findByPhoneAndIdNot(String phone, UUID id);
}
