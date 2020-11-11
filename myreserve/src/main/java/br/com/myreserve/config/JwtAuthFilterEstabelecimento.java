package br.com.myreserve.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.myreserve.services.EstabelecimentoService;
import br.com.myreserve.services.JwtService;


public class JwtAuthFilterEstabelecimento extends OncePerRequestFilter{
	private JwtService jwtService;
	private EstabelecimentoService estabelecimentoService;
	
	public JwtAuthFilterEstabelecimento(JwtService jwtService, EstabelecimentoService estabelecimentoService) {
		this.jwtService = jwtService;
		this.estabelecimentoService = estabelecimentoService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = httpServletRequest.getHeader("Authorization");
		
		if(authorization != null && authorization.startsWith("Bearer")) {
			String token = authorization.split(" ")[1];
			boolean isValid = jwtService.tokenValido(token);
			
			if(isValid) {
				String loginEstab = jwtService.obterLoginUsuario(token);
				UserDetails estab = estabelecimentoService.loadUserByUsername(loginEstab);
				UsernamePasswordAuthenticationToken user = new	
						UsernamePasswordAuthenticationToken(estab, null, estab.getAuthorities());
				
				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				SecurityContextHolder.getContext().setAuthentication(user);
			}
		}
		
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
	
}