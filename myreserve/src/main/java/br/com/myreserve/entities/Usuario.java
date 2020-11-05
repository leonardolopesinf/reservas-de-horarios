package br.com.myreserve.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Usuario {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_usuario;
	@Column(name = "nome_usuario")
	private String nome;
	private String cpf;
	private Date dt_nasc;
	private String email;
	private String telefone;
	private String img_perfil;
	private String senha;
	private Boolean user_ativo;
	
	@OneToMany(mappedBy="usuario")
	@JsonIgnoreProperties("usuario")
	private Set<Reserva> reservaUser;
	
	public Set<Reserva> getReserva() {
		return reservaUser;
	}
	
	public void setTelefone(Set<Reserva> reservaUser) {
		this.reservaUser = reservaUser;
	}
	
	public Usuario() {}

	public Usuario(String nome, String cpf, Date dt_nasc, String email,
			String telefone, String img_perfil, String senha, Boolean user_ativo) {
		this.nome = nome;
		this.cpf = cpf;
		this.dt_nasc = dt_nasc;
		this.email = email;
		this.telefone = telefone;
		this.img_perfil = img_perfil;
		this.senha = senha;
		this.user_ativo = user_ativo;
	}

	public Integer getId_usuario() {
		return id_usuario;
	}
	
	public String getNome_usuario() {
		return nome;
	}

	public void setNome_usuario(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDt_nasc() {
		return dt_nasc;
	}

	public void setDt_nasc(Date dt_nasc) {
		this.dt_nasc = dt_nasc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getImg_perfil() {
		return img_perfil;
	}

	public void setImg_perfil(String img_perfil) {
		this.img_perfil = img_perfil;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getUser_ativo() {
		return user_ativo;
	}

	public void setUser_ativo(Boolean user_ativo) {
		this.user_ativo = user_ativo;
	}

	
}