package br.com.finchsolucoes.financeiro.service.core.handlers.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;


@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9035301634732611556L;

    private Integer code = 400;

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
