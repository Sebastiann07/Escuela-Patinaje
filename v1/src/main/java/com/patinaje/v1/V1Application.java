package com.patinaje.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.patinaje.v1.model.userModel;
import com.patinaje.v1.repository.userRepository;

@SpringBootApplication
public class V1Application {

	public static void main(String[] args) {
		SpringApplication.run(V1Application.class, args);
		System.out.println("La aplicación V1 ha iniciado correctamente.");
	}

}

// Inicializador de datos: crear administrador por defecto si no existe
@org.springframework.context.annotation.Configuration
class V1DataInitializer {

	@Bean
	CommandLineRunner initAdmin(userRepository users, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminEmail = "admin@skatepro.com";
			if (!users.existsByEmail(adminEmail)) {
				userModel admin = new userModel();
				admin.setName("Administrador");
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode("Admin123!"));
				admin.setRol(userModel.Role.ADMIN);
				admin.setActivo(true);
				users.save(admin);
				System.out.println("[Seed] Usuario ADMIN creado: " + adminEmail);
			} else {
				System.out.println("[Seed] Usuario ADMIN ya existe: " + adminEmail);
			}
		};
	}
}
//.