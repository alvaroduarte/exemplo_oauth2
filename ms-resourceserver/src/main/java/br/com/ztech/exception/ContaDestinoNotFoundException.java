package br.com.ztech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Agencia e Conta destino não encontrada!")
public class ContaDestinoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7227212838595641219L;
	
	public ContaDestinoNotFoundException() {
        super();
    }
    public ContaDestinoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ContaDestinoNotFoundException(String message) {
        super(message);
    }
    public ContaDestinoNotFoundException(Throwable cause) {
        super(cause);
    }
}