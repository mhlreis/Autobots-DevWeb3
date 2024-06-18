package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;

public interface RepositorioCredencial extends JpaRepository<CredencialUsuarioSenha, Long> {

}
