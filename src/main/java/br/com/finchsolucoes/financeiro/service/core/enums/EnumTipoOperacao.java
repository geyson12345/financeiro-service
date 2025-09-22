package br.com.finchsolucoes.financeiro.service.core.enums;

import java.util.stream.*;
import lombok.*;

@Getter
public enum EnumTipoOperacao {

    CREDITO(1, "Credito"),
    DEBITO(2, "Debito");

    private final int id;
    private String operacao;

    EnumTipoOperacao(int id, String operacao) {
        this.id = id;
        this.operacao = operacao;
    }

    public static EnumTipoOperacao getById(int id) {
        return Stream.of(EnumTipoOperacao.values())
                .filter(e -> id == e.getId()).findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(EnumTipoOperacao.class, String.valueOf(id)));
    }

    public static EnumTipoOperacao getByStatus(String operacao) {
        return Stream.of(EnumTipoOperacao.values())
                .filter(e -> operacao.equalsIgnoreCase(e.getOperacao())).findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(EnumTipoOperacao.class, operacao));
    }

    void setStatus(String operacao) {
        this.operacao = operacao;
    }

}
