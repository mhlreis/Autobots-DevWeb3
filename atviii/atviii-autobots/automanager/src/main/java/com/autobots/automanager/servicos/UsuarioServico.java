package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioCredencial;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioServico {

	@Autowired
	private RepositorioUsuario repositorio;
	
	@Autowired
	private RepositorioEmail repositorioEmail;
	
	@Autowired
	private RepositorioDocumento repositorioDocumento;
	
	@Autowired
	private RepositorioCredencial repositorioCredencialUsuarioSenha;
	
	@Autowired
	private MercadoriaServico servicoMercadoria;
	
	public List<Usuario> pegarTodosUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		return usuarios;
	}
	
	public Usuario pegarPeloId(Long id) {
		Usuario usuario = repositorio.findById(id).orElse(null);
		return usuario;
	}
	
	public void cadastrarUsuario(Usuario usuario) {
		repositorio.save(usuario);
	}
	
	public void deletarUsuario(Usuario usuario) {
		System.out.println(usuario);
		repositorio.delete(usuario);
	}
	
	public Usuario atualizarUsuario(Usuario obj) {
		Usuario newObj = pegarPeloId(obj.getId());
		updateData(newObj, obj);
		for(Documento docs: obj.getDocumentos()) {
			docs.setId(docs.getId());
			updateDocumento(docs);
		}
		for(Email emails: obj.getEmails()) {
			emails.setId(emails.getId());
			updateDocumento(emails);
		}
		return repositorio.save(newObj);
	}
	
	private void updateData(Usuario newObj, Usuario obj) {
		newObj.setNome(obj.getNome());
		newObj.setNomeSocial(obj.getNomeSocial());
	}
	
	// Emails
	
	public List<Email> pegarEmails(){
		List<Email> emails = repositorioEmail.findAll();
		return emails;
	}
	
	public Email pegarPeloIdEmail(Long id) {
		Email achar = repositorioEmail.getById(id);
		return achar;
	}
	
	public void salvarEmail(Email email) {
		repositorioEmail.save(email);
	}
	
	
	public Email updateDocumento(Email obj) {
		Email newObj = pegarPeloIdEmail(obj.getId());
		updateDataEmail(newObj, obj);
		return repositorioEmail.save(newObj);
	}
	
	private void updateDataEmail(Email newObj, Email obj) {
		newObj.setEndereco(obj.getEndereco());
	}
	
	// Documentos
	

	
	public List<Documento> pegarDocumentos(){
		List<Documento> documentos = repositorioDocumento.findAll();
		return documentos;
	}
	
	public Documento pegarPeloIdDoc(Long id) {
		Documento achar = repositorioDocumento.getById(id);
		return achar;
	}
	
	public Long salvarDocumento(Documento documento) {
		return repositorioDocumento.save(documento).getId();
		
	}
	
	
	public Documento updateDocumento(Documento obj) {
		Documento newObj = pegarPeloIdDoc(obj.getId());
		updateDataDocumento(newObj, obj);
		return repositorioDocumento.save(newObj);
	}
	
	private void updateDataDocumento(Documento newObj, Documento obj) {
		newObj.setNumero(obj.getNumero());
		newObj.setDataEmissao(new Date());
	}
	
	// Usuario Senha
	
	public List<CredencialUsuarioSenha> credencial() {
		return repositorioCredencialUsuarioSenha.findAll();
	}
	
	// Mercadoria
	public void deletarMercadoria(Long idCliente, Long idMercadoria) {
		Usuario selecionado = pegarPeloId(idCliente);
		Mercadoria mercadoria = servicoMercadoria.pegarPeloId(idMercadoria); 
		if(selecionado.getId() == idCliente) {
			selecionado.getMercadorias().remove(mercadoria);
		}
	}
	
	
}