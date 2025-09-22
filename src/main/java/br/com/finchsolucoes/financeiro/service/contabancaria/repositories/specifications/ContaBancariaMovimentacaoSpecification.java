package br.com.finchsolucoes.financeiro.service.contabancaria.repositories.specifications;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancariaMovimentacao;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ContaBancariaMovimentacaoSpecification {

    public static Specification<ContaBancariaMovimentacao> contaIdEquals(Long contaId) {
        return (root, query, cb) ->
                cb.equal(root.get("contaBancaria").get("id"), contaId);
    }

    public static Specification<ContaBancariaMovimentacao> dataOperacaoBetween(LocalDateTime inicio, LocalDateTime fim) {
        return (root, query, cb) ->
                cb.between(root.get("dataOperacao"), inicio, fim);
    }

    public static Specification<ContaBancariaMovimentacao> lancamentoFuturoEquals(Boolean lancamentoFuturo) {
        return (root, query, cb) ->
                lancamentoFuturo == null ? cb.conjunction() : cb.equal(root.get("lancamentoFuturo"), lancamentoFuturo);
    }
}


