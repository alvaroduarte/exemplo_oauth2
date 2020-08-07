package br.com.ztech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Agencia e Conta não encontrada!")
public class ContaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7227212838595641219L;
	
	public ContaNotFoundException() {
        super();
    }
    public ContaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ContaNotFoundException(String message) {
        super(message);
    }
    public ContaNotFoundException(Throwable cause) {
        super(cause);
    }
}