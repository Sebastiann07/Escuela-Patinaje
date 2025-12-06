package com.patinaje.v1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (sin autenticación)
                .requestMatchers(
                    "/",
                    "/index",
                    "/index/**",
                    "/home",
                    "/registro",
                    "/login"
                ).permitAll()
                // Recursos estáticos
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/static/**",
                    "/webjars/**"
                ).permitAll()
                // Listados públicos de lectura
                .requestMatchers("/users/listar", "/instructores/listar", "/clases/listar").permitAll()
                // Rutas OpenAPI / Swagger (permitir acceso público)
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/webjars/**",
                    "/swagger-resources/**"
                ).permitAll()
                
                // Rutas solo para ADMIN
                .requestMatchers("/users/**").hasAuthority("ADMIN")
                
                // Asistencia: solo INSTRUCTOR y ADMIN
                .requestMatchers("/asistencia/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                
                // Rutas para INSTRUCTOR
                .requestMatchers("/instructores/**").hasAnyAuthority("INSTRUCTOR", "ADMIN")
                
                // Rutas para ALUMNO (y superiores)
                .requestMatchers("/alumno/**").hasAnyAuthority("ALUMNO", "INSTRUCTOR", "ADMIN")
                // Inscripción a clases por alumnos
                .requestMatchers("/clases/inscribir/**", "/clases/cancelar/**").hasAnyAuthority("ALUMNO", "ADMIN")
                
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); // Deshabilitar CSRF para APIs

        return http.build();
    }

    // Ignorar completamente filtros de seguridad para recursos estáticos
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
            "/css/**",
            "/js/**",
            "/images/**",
            "/static/**",
            "/webjars/**"
        );
    }
}