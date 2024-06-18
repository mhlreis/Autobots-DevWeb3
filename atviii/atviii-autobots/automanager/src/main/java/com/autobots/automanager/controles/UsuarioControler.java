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

import com.autobots.automanager.entitades.CredencialUsuarioSenha;

import com.autobots.automanager.entitades.Empresa;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.link.UsuarioLink;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/usuario")
public class UsuarioControler {

	@Autowired
	private UsuarioServico usuarioServico;
	
	@Autowired
	private EmpresaServico empresaServico;
	
	@Autowired
	private VendaServico vendaServico;
	
	@Autowired
	private VeiculoServico veiculoServico;
	
	@Autowired
	private UsuarioLink usuarioLink;
	
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario, @PathVariable Long id){
		Empresa empresaSelecionada = empresaServico.pegarPeloId(id);
		
		if(empresaSelecionada != null) {
	        if(usuario.getPerfis().toString().contains("FORNECEDOR")) {
	        	if(usuario.getMercadorias().size() > 0)
	        	empresaSelecionada.getMercadorias().addAll(usuario.getMercadorias());
	        }	
	        
	        usuarioServico.cadastrarUsuario(usuario);
	        
	        empresaSelecionada.getUsuarios().add(usuario);
	        empresaServico.salvar(empresaSelecionada);
	        
	        return new ResponseEntity<> (HttpStatus.CREATED);
	        
		}
			return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        	
	}
	
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Usuario>> pegarTodos(){
		List<Usuario> usuario = usuarioServico.pegarTodosUsuarios();
		usuarioLink.adicionarLink(usuario);
		return new ResponseEntity<List<Usuario>>(usuario, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Usuario> pegarUm(@PathVariable Long id){
		Usuario usuario = usuarioServico.pegarPeloId(id);
		HttpStatus status = HttpStatus.CONFLICT;
		if(usuario == null) {
			status = HttpStatus.NOT_FOUND;	
		}else{
			usuarioLink.adicionarLink(usuario);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Usuario>(usuario, status);
	}
	
	@PutMapping("/cadastrar-credencial/{id}")
	public ResponseEntity<?> cadastroCredencial(@PathVariable Long id, @RequestBody CredencialUsuarioSenha credencialUsuario){
		Usuario usuario = usuarioServico.pegarPeloId(id);
		if(usuario == null) {
			  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		usuario.getCredenciais().add(credencialUsuario);
		usuarioServico.cadastrarUsuario(usuario);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id){
		Usuario usuarioSelecionado = usuarioServico.pegarPeloId(id);
		if (usuarioSelecionado == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		for (Empresa empresa : empresaServico.pegarTodasEmpresas()) {
			for(Usuario funcionario : empresa.getUsuarios()) {
				if(funcionario.getId() == usuarioSelecionado.getId()) {
					empresa.getUsuarios().remove(funcionario);
				}
			}
		}
		
		 for(Venda venda : vendaServico.pegarTodasVendas()) {
			 if(venda.getFuncionario().getId() == usuarioSelecionado.getId()) {
				 venda.setFuncionario(null);
			 }
			 if(venda.getCliente().getId() == usuarioSelecionado.getId()) {
				 venda.setCliente(null);
			 }
		 }
		
		for (Veiculo veiculo : veiculoServico.pegarTodosVeiculos()) {
			if(veiculo.getProprietario().getId() == usuarioSelecionado.getId()) {
				veiculo.setProprietario(null);
			}
		}
		
		usuarioSelecionado.getDocumentos().removeAll(usuarioSelecionado.getDocumentos());
		usuarioSelecionado.getTelefones().removeAll(usuarioSelecionado.getTelefones());
		usuarioSelecionado.getEmails().removeAll(usuarioSelecionado.getEmails());
		usuarioSelecionado.getCredenciais().removeAll(usuarioSelecionado.getCredenciais());
		usuarioSelecionado.getMercadorias().removeAll(usuarioSelecionado.getMercadorias());
		usuarioSelecionado.getVeiculos().removeAll(usuarioSelecionado.getVeiculos());
		usuarioSelecionado.getVendas().removeAll(usuarioSelecionado.getVendas());
		usuarioSelecionado.setEndereco(null);
		
		
		usuarioServico.deletarUsuario(usuarioSelecionado);
		
		return new ResponseEntity<>(HttpStatus.OK);
			
		}

}
