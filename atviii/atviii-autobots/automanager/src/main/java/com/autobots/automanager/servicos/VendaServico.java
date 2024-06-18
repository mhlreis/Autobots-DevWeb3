package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@Service
public class VendaServico {
	@Autowired
	  private RepositorioVenda repositorio;

	  public List<Venda> pegarTodasVendas() {
	    List<Venda> pegarTodas = repositorio.findAll();
	    return pegarTodas;
	  }

	  public void cadastrarVenda(Venda venda) {
	    repositorio.save(venda);
	  }

	  public Venda pegarPeloId(Long id) {
	    Venda venda = repositorio.findById(id).orElse(null);
	    return venda;
	  }

	  public Venda atualizarVenda(Venda obj) {
	    Venda newObj = pegarPeloId(obj.getId());
	    atualizar(newObj, obj);
	    return repositorio.save(newObj);
	  }

	  private void atualizar(Venda newObj, Venda obj) {
	    newObj.setCadastro(new Date());
	    newObj.setFuncionario(obj.getFuncionario());
	    newObj.setIdentificacao(obj.getIdentificacao());
	    newObj.setVeiculo(obj.getVeiculo());
	    newObj.setCliente(obj.getCliente());
	  }
}
