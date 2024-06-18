package com.autobots.automanager.link;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControler;
import com.autobots.automanager.entitades.Usuario;

@Component
public class UsuarioLink implements AdicionadorLink<Usuario> {
	@Override
	public void adicionarLink( List<Usuario> lista ) {
		for (Usuario cliente : lista) {
			Link linkUsuario =  WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControler.class)
							.pegarUm(cliente.getId()))
					.withSelfRel();
			cliente.add(linkUsuario);
		}
	}
	
	@Override
	public void adicionarLink( Usuario usuario ) {
			Link linkUsuario =  WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControler.class)
							.pegarTodos())
					.withRel("Todos Veiculos");
			usuario.add(linkUsuario);
	}
}
