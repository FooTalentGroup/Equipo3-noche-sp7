package com.stockia.stockia.repositories;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    @Query("""
    SELECT u FROM User u
    WHERE
        (:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND
        (:name IS NULL OR :name = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND
        (:role IS NULL OR u.role = :role) AND
        (:accountStatus IS NULL OR u.account_status = :accountStatus)
""")
    Page<User> searchUsers(
            @Param("email") String email,
            @Param("name") String name,
            @Param("role") Role role,
            @Param("accountStatus") AccountStatus accountStatus,
            Pageable pageable
    );

    boolean existsById( UUID responsibleId);
}
