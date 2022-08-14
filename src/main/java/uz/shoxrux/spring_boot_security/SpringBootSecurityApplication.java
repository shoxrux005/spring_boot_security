package uz.shoxrux.spring_boot_security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.shoxrux.spring_boot_security.domains.AuthUser;
import uz.shoxrux.spring_boot_security.repository.AuthUserRepository;

import java.util.List;

@SpringBootApplication
public class SpringBootSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityApplication.class, args);
    }
    @Bean
    CommandLineRunner runner(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            authUserRepository.deleteAll();
            AuthUser admin = AuthUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123"))
                    .role(AuthUser.UserRole.ADMIN).build();
            AuthUser manager = AuthUser.builder()
                    .username("manager")
                    .password(passwordEncoder.encode("123"))
                    .role(AuthUser.UserRole.MANAGER).build();
            AuthUser user = AuthUser.builder()
                    .username("user")
                    .password(passwordEncoder.encode("123"))
                    .role(AuthUser.UserRole.USER).build();
            authUserRepository.saveAll(List.of(admin, manager, user));
        };
    }
}
