package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.link.VeiculoLink;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControler {
	
	@Autowired
	private VeiculoServico veiculosServico;
	
	@Autowired
	private VendaServico vendaServico;
	
	@Autowired
	private UsuarioServico usuarioServico;
	
	@Autowired
	private VeiculoLink veiculoLink;
	
	@PostMapping("cadastrar/{id}")
	public ResponseEntity<?> cadastrar (@RequestBody Veiculo veiculo, @PathVariable Long id){
		Usuario usuario = usuarioServico.pegarPeloId(id);
		if(usuario == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			usuario.getVeiculos().add(veiculo);
			veiculo.setProprietario(usuario);
			veiculosServico.cadastrarVeiculo(veiculo);
			usuarioServico.cadastrarUsuario(usuario);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Veiculo>> pegarTodos(){
		List<Veiculo> veiculo = veiculosServico.pegarTodosVeiculos();
		veiculoLink.adicionarLink(veiculo);
		return new ResponseEntity<List<Veiculo>>(veiculo, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Veiculo> pegarUm(@PathVariable Long id){
		Veiculo veiculo = veiculosServico.pegarPeloId(id);
		HttpStatus status = HttpStatus.CONFLICT;
		if(veiculo == null) {
			status = HttpStatus.NOT_FOUND;	
		}else{
			veiculoLink.adicionarLink(veiculo);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Veiculo>(veiculo, status);
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarVeiculo (@RequestBody Veiculo veiculo, @PathVariable Long id){
		Veiculo vec = veiculosServico.pegarPeloId(id);
		if (vec == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		veiculo.setId(id);
		veiculosServico.atualizarVeiculo(veiculo);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id){
		Veiculo veiculoSelecionado = veiculosServico.pegarPeloId(id);
		if(veiculoSelecionado == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Usuario> usuarios = usuarioServico.pegarTodosUsuarios();
		List<Venda> vendas = vendaServico.pegarTodasVendas();
		
		for(Usuario usuario : usuarios) {
			for(Veiculo veiculo: usuario.getVeiculos()) {
				if(veiculo.getId() == id) {
					usuario.getVeiculos().remove(veiculo);
					usuarioServico.cadastrarUsuario(usuario);
				}
			}
		}
		
		for(Venda venda : vendas) {
				if(venda.getVeiculo().getId() == id) {
					venda.setVeiculo(null);
					vendaServico.cadastrarVenda(venda);
				}
			}
		
		veiculosServico.deletar(veiculoSelecionado);
		return new ResponseEntity<>(HttpStatus.OK);
		
		}
		
	
}

