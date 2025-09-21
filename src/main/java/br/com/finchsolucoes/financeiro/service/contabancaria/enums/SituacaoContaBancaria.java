package br.com.finchsolucoes.financeiro.service.contabancaria.enums;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum SituacaoContaBancaria {

    ATIVA(1, "Ativa"),
    INATIVA(2, "Inativa"),
    BLOQUEADA(3, "Bloqueada");

    private final int id;
    private String status;

    SituacaoContaBancaria(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public static SituacaoContaBancaria getById(int id) {
        return Stream.of(SituacaoContaBancaria.values())
                .filter(e -> id == e.getId())
                .findAny()
                .orElseThrow(() ->
                        new EnumConstantNotPresentException(SituacaoContaBancaria.class, String.valueOf(id)));
    }

    public static SituacaoContaBancaria getByStatus(String status) {
        return Stream.of(SituacaoContaBancaria.values())
                .filter(e -> status.equalsIgnoreCase(e.getStatus()))
                .findAny()
                .orElseThrow(() ->
                        new EnumConstantNotPresentException(SituacaoContaBancaria.class, status));
    }

    void setStatus(String status) {
        this.status = status;
    }
}


