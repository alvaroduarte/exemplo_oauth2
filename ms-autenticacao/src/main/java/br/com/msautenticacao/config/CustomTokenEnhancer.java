package br.com.msautenticacao.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import br.com.msautenticacao.model.User;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {
	
	//Nesse metodo adicionamos o cpf e o codigo do credor no payload do tokens
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		//Esse comando adiciona o e-mail ou qualuqer outro atributo
		//no mesmo Rest de retorna do token e o refresh_token
		//EX. info.put("email", user.getEmail());
		
		User user = (User) authentication.getPrincipal();

		Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

		info.put("cpf", user.getCpf());
		info.put("codigo_credor", user.getCodigoCredor());

		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
		customAccessToken.setAdditionalInformation(info);

		return super.enhance(customAccessToken, authentication);
		
	}
	
}