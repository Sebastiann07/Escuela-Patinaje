package com.patinaje.v1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class userModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "nombre", nullable = true, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    String name;

    @Column(name = "e_mail", nullable = false, length = 40)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    String email;

    @NotBlank(message = "La contraseña es obligatoria ")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password;

    @Column(name = "telefono", nullable = true, length = 15)
    String phone;
}
