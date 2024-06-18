package com.autobots.automanager.link;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entitades.Empresa;

@Component
public class EmpresaLink implements AdicionadorLink<Empresa> {

	@Override
	public void adicionarLink(List<Empresa> lista) {
		for (Empresa empresa : lista) {
			Link linkEmpresa = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(EmpresaControle.class)
							.pegarUsuarioEspecifico(empresa.getId()))
					.withSelfRel();
			empresa.add(linkEmpresa);
		}
	}
	
	@Override
	public void adicionarLink(Empresa empresa) {
		Link linkEmpresa = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.pegarTodos())
				.withRel("Todas Empresas");
		empresa.add(linkEmpresa);
	}
}
