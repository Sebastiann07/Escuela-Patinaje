package com.patinaje.v1.config;

import com.patinaje.v1.model.userModel;
import com.patinaje.v1.repository.userRepository;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userModel user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

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
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities
        );
    }
}