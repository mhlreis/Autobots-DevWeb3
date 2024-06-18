package com.autobots.automanager.controles;

import java.util.Date;
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
import com.autobots.automanager.link.EmpresaLink;
import com.autobots.automanager.servicos.EmpresaServico;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {
	
	@Autowired
	private EmpresaServico servicoEmpresa;
	
	@Autowired
	private EmpresaLink empresaLink;

	@PostMapping("/cadastrar")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa){
		empresa.setCadastro(new Date());
		servicoEmpresa.salvar(empresa);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Empresa>> pegarTodos(){
		List<Empresa> todos = servicoEmpresa.pegarTodasEmpresas();
		HttpStatus status = HttpStatus.CONFLICT;
		if(todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Empresa>>(status);
		}else {
			status = HttpStatus.FOUND;
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<List<Empresa>>(todos, status);
			empresaLink.adicionarLink(todos);
			return resposta;
		}
	}

	@GetMapping("/buscar/{id}")
	public ResponseEntity<Empresa> pegarUsuarioEspecifico(@PathVariable Long id){
		Empresa empresa = servicoEmpresa.pegarPeloId(id);
		if(empresa == null) {
			return new ResponseEntity<Empresa>(HttpStatus.NOT_FOUND);
		}else {
			empresaLink.adicionarLink(empresa);
			return new ResponseEntity<Empresa>(empresa, HttpStatus.FOUND);
		}
	}
	
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa){
		Empresa emp = servicoEmpresa.pegarPeloId(id);
		if(emp == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		empresa.setId(id);
		servicoEmpresa.atualizarEmpresa(emp);
		servicoEmpresa.salvar(empresa);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id){
		Empresa empresaSelecionada = servicoEmpresa.pegarPeloId(id);
		if(empresaSelecionada == null) {
			return new ResponseEntity<Empresa>(HttpStatus.NOT_FOUND);
		}
		servicoEmpresa.deletar(empresaSelecionada);
		return new ResponseEntity<>(HttpStatus.OK);
	
	}
}