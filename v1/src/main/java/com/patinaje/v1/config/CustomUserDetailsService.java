package com.patinaje.v1.config;

import com.patinaje.v1.model.userModel;
import com.patinaje.v1.model.instructorModel;
import com.patinaje.v1.repository.userRepository;
import com.patinaje.v1.repository.instructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private instructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Primero buscar como usuario
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            userModel user = userOpt.get();
            if (!user.isActivo()) {
                throw new UsernameNotFoundException("Usuario inactivo: " + email);
            }
            List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRol().name())
            );
            return new User(
                user.getEmail(),
                user.getPassword(),
                user.isActivo(),
                true,
                true,
                true,
                authorities
            );
        }

        // Si no es usuario, intentar como instructor
        instructorModel instructor = instructorRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Cuenta no encontrada: " + email));

        // Estado: usamos campo 'estado' (Activo/Inactivo). Considerar inactivo si distinto de 'Activo'
        boolean activo = "Activo".equalsIgnoreCase(instructor.getEstado());
        if (!activo) {
            throw new UsernameNotFoundException("Instructor inactivo: " + email);
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("INSTRUCTOR")
        );

        return new User(
            instructor.getEmail(),
            instructor.getPassword(),
            true,
            true,
            true,
            true,
            authorities
        );
    }
}