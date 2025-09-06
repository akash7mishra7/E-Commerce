package com.ecom.config;

import com.ecom.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ApiSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // applies only to REST APIs
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public APIs
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                // Protected APIs
                .requestMatchers("/api/user/**").hasRole("USER")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Any other /api/** must be authenticated
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((req, res, ex2) -> {
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        res.setContentType("application/json");
                        res.getWriter().write("{\"error\": \"Unauthorized\"}");
                    })
                    .accessDeniedHandler((req, res, ex2) -> {
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        res.setContentType("application/json");
                        res.getWriter().write("{\"error\": \"Forbidden - insufficient privileges\"}");
                    })
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
