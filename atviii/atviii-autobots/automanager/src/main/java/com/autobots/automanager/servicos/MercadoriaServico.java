package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@Service
public class MercadoriaServico {

	@Autowired
	private RepositorioMercadoria repositorio;
	
	public Long salvar(Mercadoria cadastro) {
		return repositorio.save(cadastro).getId();
	}
	
	public List<Mercadoria> pegarTodos(){
		List<Mercadoria> pegarTodas = repositorio.findAll();
		return pegarTodas;
	}
	
	public Mercadoria pegarPeloId(Long id) {
		Mercadoria achar = repositorio.findById(id).orElse(null);
		return achar;
	}
	
	public Mercadoria update(Mercadoria obj) {
		Mercadoria newObj = pegarPeloId(obj.getId());
		updateData(newObj, obj);
		return repositorio.save(newObj);
	}
	
	public void deletar(Mercadoria mercadoria) {
		repositorio.delete(mercadoria);
	}
	
	private void updateData(Mercadoria newObj, Mercadoria obj) {
		newObj.setNome(obj.getNome());
		newObj.setDescricao(obj.getDescricao());
		newObj.setCadastro(obj.getCadastro());
		newObj.setFabricao(obj.getFabricao());
		newObj.setQuantidade(obj.getQuantidade());
		newObj.setValidade(obj.getValidade());
		newObj.setValor(obj.getValor());
	}
	
}