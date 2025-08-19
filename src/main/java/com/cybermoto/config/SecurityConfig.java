package com.cybermoto.config;

import com.cybermoto.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilter(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> {
                    String path = request.getRequestURI();
                    return path.startsWith("/admin/") ||
                            path.startsWith("/estoquista/") ||
                            path.startsWith("/product/edit-product/") ||
                            path.equals("/admin/login") ||
                            path.equals("/home") ||
                            path.equals("/product/manage-products") ||
                            path.equals("/product/add-products") ||
                            path.equals("/product/edit-product");


                })

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login", "/h2-console/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/product/manage-products").hasAnyRole("ADMIN", "ESTOQUISTA")
                        .requestMatchers("/product/add-products").hasAnyRole("ADMIN", "ESTOQUISTA")
                        .requestMatchers("/product/edit-product/**").hasAnyRole("ADMIN", "ESTOQUISTA")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/estoquista/**").hasRole("ESTOQUISTA")
                        .requestMatchers("/home").hasAnyRole("ADMIN", "ESTOQUISTA")
                        .requestMatchers("/product/edit-product/**").hasAnyRole("ADMIN", "ESTOQUISTA")
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
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationManager(authenticationManager(http))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));


        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain clientFilter(HttpSecurity http,
                                            @Qualifier ("clientDetailsService")UserDetailsService clientDetailsService) throws Exception {
        http
                .securityMatcher(request -> request.getRequestURI().startsWith("/client/"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/client/mainPage",
                                "/client/add-client",
                                "/client/client-added",
                                "/client/login-client",
                                "/uploads/products/**",
                                "/css/**", "/js/**", "/images/**",
                                "/cybermoto-logo.png"
                        ).permitAll()
                        .anyRequest().hasRole("CLIENT")
                )
                .formLogin(login -> login
                        .loginPage("/client/login-client")
                        .loginProcessingUrl("/client/login-client")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/client/mainPage", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/client/logout")
                        .logoutSuccessUrl("/client/login-client?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(clientDetailsService);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
