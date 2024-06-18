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
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.link.ServicoLink;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.ServicoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/servico")
public class ServicoControler {

	@Autowired
	private ServicoServico servicoServico;
	
	@Autowired
	private EmpresaServico servicoEmpresa;
	
	@Autowired
	private VendaServico servicoVenda;
	
	@Autowired
	private ServicoLink servicoLink;
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizar(@RequestBody Servico servico ,@PathVariable Long id){
		Servico serv = servicoServico.pegarPeloId(id);
		if(serv == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		servico.setId(id);
		servicoServico.update(servico);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrar(@RequestBody Servico servico, @PathVariable Long id ){
	Empresa empresa = servicoEmpresa.pegarPeloId(id);
	if (empresa == null) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	empresa.getServicos().add(servico);
	servicoEmpresa.salvar(empresa);
	return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Servico>> pegarTodos(){
		List<Servico> servicos = servicoServico.pegarTodosServicos();
		servicoLink.adicionarLink(servicos);
		return new ResponseEntity<List<Servico>>(servicos, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Servico> pegarUm(@PathVariable Long id){
		Servico servico = servicoServico.pegarPeloId(id);
		HttpStatus status = HttpStatus.CONFLICT;
		if(servico == null) {
			status = HttpStatus.NOT_FOUND;	
		}else{
			servicoLink.adicionarLink(servico);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Servico>(servico, status);
	}	
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id){
		Servico servicoSelecionado = servicoServico.pegarPeloId(id);
		
		if(servicoSelecionado == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		List<Empresa> empresas = servicoEmpresa.pegarTodasEmpresas();
	    List<Venda> vendas = servicoVenda.pegarTodasVendas();
	    
	    for (Empresa empresa : empresas) {
	        for (Servico servico : empresa.getServicos()) {
	          if (servico.getId().equals(id)) {
	        	  empresa.getServicos().remove(servico);
	          }
	        }
	      }
	    
	      for (Venda venda : vendas) {
	        for (Servico servico : venda.getServicos()) {
	          if (servico.getId().equals(id)) {
	        	  venda.getServicos().remove(servico);
	          }
	        }
	      }
	
	    servicoServico.deletar(servicoSelecionado);
		return new ResponseEntity<>(HttpStatus.OK);
	    
	}
	
}
