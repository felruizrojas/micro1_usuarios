package com.micro1_usuarios.micro1_usuarios.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")

public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false, unique = true)
    private String nombreRol;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean rolActivo = true;

    // Método para mostrar estado como texto en JSON
    @JsonProperty("estado")
    public String getEstado() {
        return rolActivo ? "activo" : "desactivo";
    }

    // Método para filtrar permisos activos
    @JsonProperty("permisos")
    public List<Permiso> getPermisosActivos() {
        if (permisos == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(permisos.stream()
                .filter(Permiso::isPermisoActivo)
                .toList());
    }

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore // No serializar la propiedad permisos directamente
    private List<Permiso> permisos;
}
