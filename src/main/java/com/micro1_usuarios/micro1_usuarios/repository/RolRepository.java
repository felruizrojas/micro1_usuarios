package com.micro1_usuarios.micro1_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro1_usuarios.micro1_usuarios.model.Rol;

@Repository

public interface RolRepository extends JpaRepository<Rol, Integer> {
    
}
