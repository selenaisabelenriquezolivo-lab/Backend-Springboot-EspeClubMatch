package com.especlub.match.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "interest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Identificador único del interés")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Comment("Nombre del interés, e.g. Música, Tecnología")
    private String name;

    @Column(name = "description")
    @Comment("Descripción opcional del interés")
    private String description;

    @Column(name = "record_status")
    @Comment("Estado del registro (activo/inactivo).")
    private Boolean recordStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "interests")
    private Set<Student> students;
}

