package uz.shoxrux.spring_boot_security.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uz.shoxrux.spring_boot_security.service.AuthService;

import java.util.concurrent.TimeUnit;

public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    public static final String[] WHITE_LIST = {
            "/",
            "/h2/**",
            "/login/**",
            "/js/**",
            "/css/**",
            "/fail"
    };


    @Value("${REMEMBER_ME_TOKEN_SECRET}")
    public String REMEMBER_ME_TOKEN_SECRET;

    @Value("${REMEMBER_ME_TOKEN_EXPIRY_IN_DAYS}")
    public int REMEMBER_ME_TOKEN_EXPIRY_IN_DAYS = 20;

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public SecurityConfigurer(PasswordEncoder passwordEncoder, AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = authService;
    }

    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .antMatchers(WHITE_LIST).permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", false))
                .rememberMe(httpSecurityRememberMeConfigurer ->
                        httpSecurityRememberMeConfigurer
                                .rememberMeParameter("rememberMe")
                                .rememberMeCookieName("remember")
                                .key(REMEMBER_ME_TOKEN_SECRET)
                                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(REMEMBER_ME_TOKEN_EXPIRY_IN_DAYS)))
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST", true))
                                .logoutSuccessUrl("/login")
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID", "remember")
                                .invalidateHttpSession(true));
    }


}
