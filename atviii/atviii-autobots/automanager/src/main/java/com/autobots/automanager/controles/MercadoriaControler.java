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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.link.MercadoriaLink;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.MercadoriaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControler {
	@Autowired
	private MercadoriaServico servico;
	
	@Autowired
	private UsuarioServico usuarioServico;
	
	@Autowired
	private VendaServico vendaServico;
	
	@Autowired 
	EmpresaServico empresaServico;
	
	@Autowired
	private MercadoriaLink mercadoriaLink;
	
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrar(@RequestBody Mercadoria mercadoria, @PathVariable Long id){
		
		Long idMercadoria = servico.salvar(mercadoria);
		Mercadoria mercadoriaNova = servico.pegarPeloId(idMercadoria);
		
		Usuario usuario = usuarioServico.pegarPeloId(id);
		
		if(usuario == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		for(Empresa empresa : empresaServico.pegarTodasEmpresas()) {
			for (Usuario usuarios : usuarioServico.pegarTodosUsuarios()) {
				if(usuarios.getId().equals(usuario.getId())) {
					empresa.getMercadorias().add(mercadoriaNova);
					empresaServico.salvar(empresa);
				}
			}
		}
		
		usuario.getMercadorias().add(mercadoriaNova);
		usuarioServico.cadastrarUsuario(usuario);
		return new ResponseEntity<> (HttpStatus.CREATED);
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Mercadoria>> pegarTodos(){
		List<Mercadoria> mercadoria = servico.pegarTodos();
		if(mercadoria.isEmpty()) {
			return new ResponseEntity<List<Mercadoria>>(HttpStatus.NOT_FOUND);
		}
		mercadoriaLink.adicionarLink(mercadoria);
		return new ResponseEntity<List<Mercadoria>>(mercadoria, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Mercadoria> pegarUm(@PathVariable Long id){
		Mercadoria mercadoria = servico.pegarPeloId(id);
		HttpStatus status = HttpStatus.CONFLICT;
		if(mercadoria == null) {
			status = HttpStatus.NOT_FOUND;	
		}else{
			mercadoriaLink.adicionarLink(mercadoria);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Mercadoria>(mercadoria, status);
		
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria mercadoria, @PathVariable Long id){
		Mercadoria merc  = servico.pegarPeloId(id);
		if (merc == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		mercadoria.setId(id);
		servico.update(mercadoria);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> atualizarMercadoria(@PathVariable Long id){
		Mercadoria mercadoriaSelecionada = servico.pegarPeloId(id);
	
	    if(mercadoriaSelecionada == null) {
	    	return new ResponseEntity<>(HttpStatus.FOUND);
	    }
	    
	    List<Usuario> usuarios = usuarioServico.pegarTodosUsuarios();
	    List<Empresa> empresas = empresaServico.pegarTodasEmpresas();
	    List<Venda> vendas = vendaServico.pegarTodasVendas();
	    
	    for(Usuario usuario : usuarios) {
	    	for(Mercadoria mercadoria : usuario.getMercadorias()) {
	    		if(mercadoria.getId() == id) {
	    			usuario.getMercadorias().remove(mercadoria);
	    			usuarioServico.cadastrarUsuario(usuario);
	    		}
	    	}
	    }
	    
	    for(Empresa empresa : empresas) {
	    	for(Mercadoria mercadoria : empresa.getMercadorias()) {
	    		if(mercadoria.getId() == id) {
	    			empresa.getMercadorias().remove(mercadoria);
	    			empresaServico.salvar(empresa);
	    		}
	    	}
	    }
	    
	    for(Venda venda : vendas) {
	    	for(Mercadoria mercadoria : venda.getMercadorias()) {
	    		if(mercadoria.getId() == id) {
	    			venda.getMercadorias().remove(mercadoria);
	    			vendaServico.cadastrarVenda(venda);
	    		}
	    	}
	    }

	    servico.deletar(mercadoriaSelecionada);
	    return new ResponseEntity<>(HttpStatus.OK);

	}
	
}
