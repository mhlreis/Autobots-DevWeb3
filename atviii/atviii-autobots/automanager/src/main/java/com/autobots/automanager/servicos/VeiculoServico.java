package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
@Service
public class VeiculoServico {
	@Autowired
	private RepositorioVeiculo repositorio;
	
	public void cadastrarVeiculo(Veiculo veiculo) {
		repositorio.save(veiculo);
	}
	
	public List<Veiculo> pegarTodosVeiculos(){
		List<Veiculo> veiculos = repositorio.findAll();
		return veiculos;
	}
	
	public Veiculo pegarPeloId(Long id) {
		Veiculo veiculo = repositorio.findById(id).orElse(null);
		return veiculo;
	}
	
	public Veiculo atualizarVeiculo(Veiculo obj) {
		Veiculo newObj = pegarPeloId(obj.getId());
		atualizar(newObj, obj);
		return repositorio.save(newObj);
	}
	
	public void deletar(Veiculo veiculo) {
		repositorio.delete(veiculo);
	}
	
	private void atualizar(Veiculo newObj, Veiculo obj) {
		newObj.setModelo(obj.getModelo());
		newObj.setPlaca(obj.getPlaca());
		newObj.setTipo(obj.getTipo());		
	}
}
