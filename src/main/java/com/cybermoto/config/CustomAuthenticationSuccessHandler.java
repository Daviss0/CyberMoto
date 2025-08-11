package com.cybermoto.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


    @Component
    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {

            authentication.getAuthorities().forEach(role -> {
                System.out.println(">>> Role detectada: " + role.getAuthority());
            });

            for (var authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();

                if (role.equals("ROLE_ADMIN") || role.equals("ROLE_ESTOQUISTA")) {
                    response.sendRedirect("/home");
                    return;
                }
            }

            response.sendRedirect("/access-denied");
        }
    }



