package br.com.finchsolucoes.financeiro.service.contabancaria.repositories;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long>, JpaSpecificationExecutor<ContaBancaria> {

    Optional<ContaBancaria> findByNome(String nome);

    boolean existsByBancoAndAgenciaAndNumeroContaAndCodigoOperacao(Banco banco, String agencia, String numeroConta, String codigoOperacao);

    Optional<ContaBancaria> findByNomeIgnoreCase(String nome);

    boolean existsByBancoAndAgenciaAndNumeroContaAndCodigoOperacaoAndIdNot(Banco banco, String agencia, String numeroConta, String codigoOperacao, Long id);

    @Query("SELECT COALESCE(SUM(c.saldo), 0) FROM ContaBancaria c")
    BigDecimal saldoContas();

}
