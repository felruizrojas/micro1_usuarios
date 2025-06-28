package com.micro1_usuarios.micro1_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;

@Repository

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    
}
