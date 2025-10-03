package com.especlub.match.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del usuario.")
    private Long id;

    @Column(name = "username", nullable = false)
    @Comment("Nombre de usuario para login.")
    private String username;

    @Column(name = "email")
    @Comment("Correo electrónico del usuario.")
    private String email;

    @Column(name = "phone", length = 50)
    @Comment("Teléfono del usuario.")
    private String phone;

    @Column(name = "first_name")
    @Comment("Nombre del usuario.")
    private String firstName;

    @Column(name = "last_name")
    @Comment("Apellido del usuario.")
    private String lastName;

    @Column(name = "company")
    @Comment("Nombre de la empresa.")
    private String company;

    @Column(name = "first_login")
    @Comment("Indica si es el primer inicio de sesión del usuario.")
    private Boolean firstLogin;

    @Column(name = "password")
    @Comment("Contraseña encriptada.")
    private String password;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    @Comment("Fecha de creación.")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Comment("Fecha de última actualización.")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_info_roles",
        joinColumns = @JoinColumn(name = "user_info_id"),
        inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    private List<UserRole> roles;
}