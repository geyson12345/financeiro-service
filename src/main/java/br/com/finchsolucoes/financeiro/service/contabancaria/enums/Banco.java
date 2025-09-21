package br.com.finchsolucoes.financeiro.service.contabancaria.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Banco {

    CAIXA_ECONOMICA_FEDERAL(104, "Caixa Econômica Federal"),
    BANCO_DO_BRASIL(1, "Banco do Brasil"),
    BRADESCO(237, "Bradesco"),
    ITAU_UNIBANCO(341, "Itaú Unibanco"),
    SANTANDER(33, "Santander"),
    BANCO_SAFRA(422, "Banco Safra"),
    CITIBANK(745, "Citibank Brasil"),
    BTG_PACTUAL(208, "BTG Pactual"),
    NUBANK(260, "Nubank"),
    BANCO_C6(336, "Banco C6"),
    BANCO_VOTORANTIM(655, "Banco Votorantim");


    private final int codigo;
    private final String nome;

    Banco(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public static Banco getByCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(b -> b.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Banco inválido (código): " + codigo));
    }

    public static Banco getByNome(String nome) {
        return Arrays.stream(values())
                .filter(b -> b.nome.equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Banco inválido (nome): " + nome));
    }

}

