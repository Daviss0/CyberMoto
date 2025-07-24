package com.cybermoto.config;

import com.cybermoto.entity.User;
import com.cybermoto.enums.TypeUser;
import com.cybermoto.repository.UserRepository;
import com.nimbusds.oauth2.sdk.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.List;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> {
                    String path = request.getRequestURI();
                    return path.startsWith("/admin/") || path.startsWith("/estoquista/");
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/admin/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/estoquista/**").hasRole("ESTOQUISTA")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain clientFilter(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/client/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(  "/client/add-client",
                                "/client/client-added",
                                "/client/login-client",
                                "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().hasRole("CLIENT")
                )
                .formLogin(login -> login
                        .loginPage("/client/login-client")
                        .loginProcessingUrl("/client/login-client")
                        .defaultSuccessUrl("/client/home", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/client/logout"));
                return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

            System.out.println(">>> Usuário encontrado: " + email);
            System.out.println(">>> Criando UserDetails direto no SecurityConfig com role: " + user.getType());

            String role = "ROLE_" + user.getType().name();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority(role)))
                    .build();
        };
    }



    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
