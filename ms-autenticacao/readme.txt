####################################################################################################################################################
COMANDO PARA GERAR AS CHAVES PUBLICA DE ARMAZENAMENTO E O CERTIFICADO
####################################################################################################################################################

Sistema operacional : Linux
Distribuição: Ubuntu 18.04


Primeiro, devemos gerar um arquivo de armazenamento de chaves. Para fazer isso, execute o seguinte comando como root:


keytool -genkeypair -alias jwt -keyalg RSA -keypass password -keystore jwt.jks -storepass password


O comando acima irá gerar um arquivo chamado jwt.jks que contém as chaves pública e privada.
Recomenda-se migrar para o PKCS12. Para fazer isso, execute o seguinte comando root:


keytool -importkeystore -srckeystore jwt.jks -destkeystore jwt.jks -deststoretype pkcs12

O sistema pedirá a senha, basta digitar a senha: password


Agora vamos exibir a chave pública executando o seguinte comando como root:


keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey

O sistema pedirá a senha, basta digitar a senha: password

copie o arquivo jwt.jks para a pasta src/main/resources do seu projeto que vai ser o servidor de autenticação


####################################################################################################################################################
PASSO A PASSO PARA CRIAR UM RESOURCE SERVER
####################################################################################################################################################

Crie seu novo projeto como modulo maven do projeto pai bv-rd-microservicos,

utilize o comando abaixo para exibir a chave publica do seu certificado:


keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey

Obs: o comando acima precisa ser digitado dentro do diretorio onde se econtra o arquivo de certificado jwt.jks
O sistema pedirá a senha, basta digitar a senha: password

Copie a chave publica exbida com o comando acima incluindo o -------BEGIN PUBLIC KEY------- até o ------END PUBLIC KEY-------
e insira como o valor dessa chave "security.oauth2.resource.jwt.key-value=" no application.properties da aplicação que será o respurceserver,
exemplo:

application.properties - da aplicação que será um resource serve

server.port=8081
security.oauth2.resource.jwt.key-value=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgIfSIqBDyP3bJYoAJdlBoiku1upGT3lB6EIa5PdTeb+M5HOR9EOEPvRkXAAf1oZ0xK90OKu9vd0rVy9QrCJxi9Treu+enfpzJ13k6ZBghc5hsQ79mXCCV0RkkgFHeIqyVWzKk1/PQBJmaRsy87MZofGqwob7ZL6GDovs4CkWXf+JPMsfJ8S7MQoSbs9lRCkenLrQP2ha/vNXRVd/pUaMG/0g0sbaJk9fXkOgr5ZJuYjIoq3WPjtic7AhWp0jWn1NaCQM0p2OFJ4/dSH7Iy7VxIjtP25S4pn71LT6VLJJt2btObGpcH2ZEcHAFMsq/j6PWLugINcmCTqIwDG7izB7OQIDAQAB-----END PUBLIC KEY-----


no pom.xml da aplicação que será um resource server coloque a seguinte dependencia:

<dependency>
	<groupId>br.com.boavista</groupId>
	<version>0.0.1-SNAPSHOT</version>
	<artifactId>bv-rd-token</artifactId>
</dependency>

Utilize a seguinte anotação no controller da sua API para incluir a verificaçaõ oAuth2 sobre aquele metodo especifico:

@PreAuthorize("hasAnyAuthority('role_admin','role_user')")
ou
@PreAuthorize("hasAnyAuthority('role_admin')")
ou
@PreAuthorize("hasAnyAuthority('role_user')")

Obs. role_admin e role_user são as permissões de acesso conforme configurada no servidor de autenticação, exemplo:

// usuarios admins e usuarios  tem acesso - passando o token que foi pego no servidor de autenticação
@GetMapping("/users")
@PreAuthorize("hasAnyAuthority('role_admin','role_user')")
public String secured(Payload principal) {
	return principal.getUsername() + " " + principal.getEmail();
}

// somente usuarios admins tem acesso - passando o token que foi pego no servidor de autenticação
@GetMapping("/admins")
@PreAuthorize("hasAuthority('role_admin')")
public String context() {
	Payload principal = (Payload) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	return principal.getUsername() + " " + principal.getEmail();
}

// Acesso publico, qualquer tem acesso e o sistema não solicita o token de autenticação
@GetMapping("/common")
public String general() {
	return "common api success";
}