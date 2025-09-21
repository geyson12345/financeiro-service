package br.com.finchsolucoes.financeiro.service.core.handlers.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForbidenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1503608588614110651L;

    private Integer code = 400;

    public ForbidenException() {
    }

    public ForbidenException(final String message) {
        super(message);
    }

    public ForbidenException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
