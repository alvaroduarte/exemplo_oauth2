package br.com.msautenticacao.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import br.com.msautenticacao.model.User;
import br.com.msautenticacao.repository.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserRepository userRepository;
	
	
	//chamar o servico do Diorgenes aqui
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
  
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        System.out.println("name ->"+name);
        System.out.println("password ->"+password);
         
        if (true) {
        	
        	User user = userRepository.findByUsername(name);
        	
        	if(user == null) {
        		throw new BadCredentialsException("Usuario nao encontrado");
        	}
        	
        	Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        	authorities.add(new SimpleGrantedAuthority("role_default"   ));
        	authorities.add(new SimpleGrantedAuthority("can_create"   ));
        	authorities.add(new SimpleGrantedAuthority("can_update"   ));
        	authorities.add(new SimpleGrantedAuthority("can_update"   ));
        	authorities.add(new SimpleGrantedAuthority("can_read"   ));
        
            // use the credentials 
            // and authenticate against the third-party system
            return new UsernamePasswordAuthenticationToken(user, password, authorities);
            
        } else {
        	//aqui grava as informações no banco de dadoss
        	throw new BadCredentialsException("O CPF nao esta na base de dados!");
        }
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

