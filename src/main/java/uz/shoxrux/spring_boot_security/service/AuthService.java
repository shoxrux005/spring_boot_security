package uz.shoxrux.spring_boot_security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.shoxrux.spring_boot_security.config.AuthUserDetails;
import uz.shoxrux.spring_boot_security.domains.AuthUser;
import uz.shoxrux.spring_boot_security.repository.AuthUserRepository;

import java.util.function.Supplier;
@RequiredArgsConstructor
@Service("userDetailsService")
public class AuthService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<RuntimeException> userNotyFoundSupplier = () -> {
            throw new UsernameNotFoundException("User not found");
        };
        AuthUser authUser = authUserRepository.findByUsername(username).orElseThrow(userNotyFoundSupplier);
        return new AuthUserDetails(authUser);
    }
}
