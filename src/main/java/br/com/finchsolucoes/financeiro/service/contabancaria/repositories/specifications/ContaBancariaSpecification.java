package br.com.finchsolucoes.financeiro.service.contabancaria.repositories.specifications;


import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ContaBancariaSpecification {

    public static Specification<ContaBancaria> filter(
            String nome,
            Banco banco,
            String agencia,
            String numeroConta,
            String titularConta,
            SituacaoContaBancaria situacao
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (banco != null) {
                predicates.add(cb.equal(root.get("banco"), banco));
            }
            if (agencia != null && !agencia.isBlank()) {
                predicates.add(cb.equal(root.get("agencia"), agencia));
            }
            if (numeroConta != null && !numeroConta.isBlank()) {
                predicates.add(cb.equal(root.get("numeroConta"), numeroConta));
            }
            if (titularConta != null && !titularConta.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("titularConta")), "%" + titularConta.toLowerCase() + "%"));
            }
            if (situacao != null) {
                predicates.add(cb.equal(root.get("situacao"), situacao));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


