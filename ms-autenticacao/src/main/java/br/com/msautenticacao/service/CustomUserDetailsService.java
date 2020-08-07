package br.com.msautenticacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.msautenticacao.model.User;
import br.com.msautenticacao.repository.UserRepository;


@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String input) {
		
		User user = userRepository.findByUsername(input);

		if (user == null) {
		
			throw new BadCredentialsException("Usuario nao econtrado!");
		
		}
		
		new AccountStatusUserDetailsChecker().check(user);

		return user;
	}
}