package br.com.finchsolucoes.financeiro.service.core.handlers.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class EntityAlreadyExistsException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = -5251246623060863466L;

    private Integer code = 500;

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(Integer code) {
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}


