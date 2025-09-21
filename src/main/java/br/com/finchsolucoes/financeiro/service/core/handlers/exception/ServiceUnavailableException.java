package br.com.finchsolucoes.financeiro.service.core.handlers.exception;

public class ServiceUnavailableException extends RuntimeException {

    private final Integer code = 503;

    public ServiceUnavailableException() {
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
