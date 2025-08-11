package br.com.finchsolucoes.financeiro.service.contabancaria.enums;

import lombok.Getter;

@Getter
public enum Banco {
    CAIXA_ECONOMICA_FEDERAL("104", "Caixa Econômica Federal"),
    BANCO_DO_BRASIL("001", "Banco do Brasil"),
    BRADESCO("237", "Bradesco"),
    ITAU_UNIBANCO("341", "Itaú Unibanco"),
    SANTANDER("033", "Santander");

    private final String codigo;
    private final String nome;

    Banco(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }
}