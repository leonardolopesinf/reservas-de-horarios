package br.com.myreserve.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.myreserve.dto.CredenciaisDTO;
import br.com.myreserve.dto.TokenDTO;
import br.com.myreserve.entities.Estabelecimento;
import br.com.myreserve.entities.Logins;
import br.com.myreserve.exceptions.SenhaInvalidaException;
import br.com.myreserve.repositories.CategoriaRepository;
import br.com.myreserve.repositories.EstabelecimentoRepository;
import br.com.myreserve.repositories.LoginsRepository;
import br.com.myreserve.services.JwtService;
import br.com.myreserve.services.LoginsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurante")
@RequiredArgsConstructor
public class EstabelecimentoController {
	
	@Autowired
	EstabelecimentoRepository estabelecimentoRepository;
	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	LoginsRepository loginsRepository;
	@Autowired 
	LoginsService loginsService;
	@Autowired
	JwtService jwtService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping()
	public Iterable<Estabelecimento> getEstabelecimentos(Pageable pageable){
		return estabelecimentoRepository.findAll(pageable);
	}
	
	@GetMapping("/{id}")
	public Optional<Estabelecimento> getById(@PathVariable int id) {
		return estabelecimentoRepository.findById(id);
	}
	
	@PostMapping()
	public void addEstabelecimento(@RequestBody Estabelecimento estabelecimento) {
		Logins login = new Logins();
		
		String senhaCriptografada = passwordEncoder.encode(estabelecimento.getSenha()); 
		
		estabelecimento.setSenha(senhaCriptografada);
		estabelecimentoRepository.save(estabelecimento);
		
		login.setEmail(estabelecimento.getEmail());
		login.setSenha(senhaCriptografada);
		login.setIdEstabelecimento(estabelecimento.getId_estabelecimento());
		loginsRepository.save(login);
	}
	
	@PostMapping("/auth")
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			Logins login= Logins.builder()
												.email(credenciais.getLogin())
												.senha(credenciais.getSenha())
												.build();
			
			loginsService.autenticar(login);
			String token = jwtService.gerarToken(login);
			return new TokenDTO(login.getEmail(), token);
		}catch(UsernameNotFoundException | SenhaInvalidaException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@PutMapping("/{idEstab}")
	public Estabelecimento updateEstab(@PathVariable Integer idEstab, @RequestBody Estabelecimento dadosEstab) throws Exception{
		Estabelecimento estabDB = estabelecimentoRepository.findById(idEstab)
				.orElseThrow(() -> new IllegalAccessException());
		if(dadosEstab.getNome() != null) estabDB.setNome(dadosEstab.getNome());
		if(dadosEstab.getHora_funcionamento() != null) estabDB.setHora_funcionamento(dadosEstab.getHora_funcionamento());
		if(dadosEstab.getImg_estabelecimento() != null) estabDB.setImg_estabelecimento(dadosEstab.getImg_estabelecimento());
		if(dadosEstab.getDescricao() != null) estabDB.setDescricao(dadosEstab.getDescricao());
		if(dadosEstab.getMax_pessoas() != null) estabDB.setMax_pessoas(dadosEstab.getMax_pessoas());
		if(dadosEstab.getFk_categoria() != null) estabDB.setFk_categoria(dadosEstab.getFk_categoria());
		if(dadosEstab.getSenha() != null) {
			String newPassword = passwordEncoder.encode(dadosEstab.getSenha());
			
			estabDB.setSenha(newPassword);
			
			Logins login = loginsRepository.findOneByIdEstabelecimento(idEstab)
					.orElseThrow(() -> new IllegalAccessException());
			
			login.setSenha(newPassword);
			
			loginsRepository.save(login);
		};
		return estabelecimentoRepository.save(estabDB);
	}
	
}
