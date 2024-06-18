package com.autobots.automanager.link;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import com.autobots.automanager.controles.VendaControler;
import com.autobots.automanager.entitades.Venda;

@Component
public class VendaLink implements AdicionadorLink<Venda> {
	@Override
	public void adicionarLink (List <Venda> lista) {
		for (Venda vendas : lista) {
			Link linkVenda = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(VendaControler.class)
							.pegarUm(vendas.getId()))
					.withSelfRel();
			vendas.add(linkVenda);
		}
	}
	
	@Override
	public void adicionarLink (Venda venda) {
		Link linkVenda = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControler.class)
						.pegarTodos())
				.withRel("Todas Vendas");
		venda.add(linkVenda);
	}

}
