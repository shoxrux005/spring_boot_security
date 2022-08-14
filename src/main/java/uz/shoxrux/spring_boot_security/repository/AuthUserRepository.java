package uz.shoxrux.spring_boot_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.shoxrux.spring_boot_security.domains.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {
    Optional<AuthUser> findByUsername(String username);
}
