package com.example.eixo.usuarios.repository;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<Usuario> findAllByOficina_oficinaId(Long oficinaId);

}
