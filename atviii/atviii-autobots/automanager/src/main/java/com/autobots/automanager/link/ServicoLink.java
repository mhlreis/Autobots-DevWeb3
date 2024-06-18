package com.autobots.automanager.link;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControler;
import com.autobots.automanager.entitades.Servico;

@Component
public class ServicoLink implements AdicionadorLink <Servico>{

	@Override
	public void adicionarLink( List<Servico> lista ) {
		for (Servico servico : lista) {
			Link linkServico =  WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ServicoControler.class)
							.pegarUm(servico.getId()))
					.withSelfRel();
			servico.add(linkServico);
			
		}
	}
	@Override
	public void adicionarLink( Servico servico ) {
			Link linkServico =  WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ServicoControler.class)
							.pegarTodos())
					.withRel("Todos Serviços");
			servico.add(linkServico);
			
}
}
