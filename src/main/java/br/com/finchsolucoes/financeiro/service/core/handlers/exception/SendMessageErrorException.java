package br.com.finchsolucoes.financeiro.service.core.handlers.exception;

import lombok.Getter;

@Getter
public class SendMessageErrorException extends RuntimeException {
    private final transient String subject;
    private final transient String body;
    private final transient String message;

    public SendMessageErrorException(String subject, String body, String message) {
        super(message);
        this.subject = subject;
        this.body = body;
        this.message = message;
    }

}