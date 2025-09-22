package br.com.finchsolucoes.financeiro.service.contabancaria.repositories;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancariaMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ContaBancariaMovimentacaoRepository extends JpaRepository<ContaBancariaMovimentacao, Long>, JpaSpecificationExecutor<ContaBancariaMovimentacao> {


    Optional<ContaBancariaMovimentacao> findById(Long id);


}
