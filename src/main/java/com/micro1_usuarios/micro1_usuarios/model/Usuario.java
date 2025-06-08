package com.micro1_usuarios.micro1_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // en una entidad de intersección se crearía una clase @Data sin @Entity y se le asignaría la tabla de intersección
@Table(name = "usuarios")

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 13, nullable = false, unique = true)
    private String user;

    @Column(length = 50, nullable = false)
    private String pass;

    @Column(nullable = false)
    private boolean usuarioActivo = true;

    @Column(length = 13, nullable = false)
    private String run;

    @Column(length = 50, nullable = false)
    private String pNombre;

    @Column(length = 50)
    private String sNombre;

    @Column(length = 50, nullable = false)
    private String pApellido;

    @Column(length = 50, nullable = false)
    private String sApellido;

    @Column(length = 100, nullable = false)
    private String correo;

    @Column(length = 250, nullable = false)
    private String direccion;

    @Column(length = 250, nullable = false)
    private String ciudad;

    @Column(length = 250, nullable = false)
    private String region;

}
