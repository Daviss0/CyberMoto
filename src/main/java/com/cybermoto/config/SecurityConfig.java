package com.cybermoto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**","/admin/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/estoquista").hasRole("ESTOQUISTA")
                        .anyRequest().authenticated()
                )

                .formLogin(login ->login // apos o login bem-sucedido o usuario vai ser redirecionado para essa url
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/home",true)
                        .permitAll()
                )
                .userDetailsService(userDetailService)
                .csrf(csrf ->csrf  //desativa a proteção csrf, o console web nao funciona corretamente com o csrf ativado
                        .disable()
                )
                .headers(headers -> headers //desativa o frameoption para nao bloquear o carregamento da interface do h2
                        .frameOptions(frame -> frame.disable())
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
