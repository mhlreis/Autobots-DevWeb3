package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.link.VendaLink;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;


@RestController
@RequestMapping("/venda")

public class VendaControler {

	@Autowired
	private VendaServico vendaServico;
	
	@Autowired
	private EmpresaServico empresaServico;
	
	@Autowired
	private VeiculoServico veiculoServico;
	
	@Autowired
	private UsuarioServico usuarioServico;
	
	@Autowired
	private VendaLink linkVenda;
	
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrar(@RequestBody Venda venda, @PathVariable Long id){
		Empresa empresaSelecionada = empresaServico.pegarPeloId(id);
		if(empresaSelecionada == null) {
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Usuario funcionarioSelecionado = usuarioServico.pegarPeloId(venda.getFuncionario().getId());
		Usuario clienteSelecionado = usuarioServico.pegarPeloId(venda.getCliente().getId());
		Veiculo veiculoSelecionado = veiculoServico.pegarPeloId(venda.getVeiculo().getId());
		venda.setVeiculo(veiculoSelecionado);
		venda.setCliente(clienteSelecionado);
		venda.setFuncionario(funcionarioSelecionado);
		usuarioServico.cadastrarUsuario(funcionarioSelecionado);

		empresaSelecionada.getVendas().add(venda);
		empresaServico.salvar(empresaSelecionada);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	@GetMapping("/buscar")
	public ResponseEntity<List<Venda>> pegarTodos(){
		List<Venda> venda = vendaServico.pegarTodasVendas();
		linkVenda.adicionarLink(venda);
		return new ResponseEntity<List<Venda>>(venda, HttpStatus.FOUND);
	}
	
	@GetMapping("/buscar/{id}")
	public ResponseEntity<Venda> pegarUm(@PathVariable Long id){
		Venda venda = vendaServico.pegarPeloId(id);
		HttpStatus status = HttpStatus.CONFLICT;
		if(venda == null) {
			status = HttpStatus.NOT_FOUND;	
		}else{
			linkVenda.adicionarLink(venda);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Venda>(venda, status);
	}

	@PutMapping("/atualizar/{id}")
		public ResponseEntity<?> atualizarVenda (@RequestBody Venda venda, @PathVariable Long id){
			Venda vend = vendaServico.pegarPeloId(id);
			if (vend == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			venda.setId(id);
			vendaServico.atualizarVenda(venda);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	
	
	
}
