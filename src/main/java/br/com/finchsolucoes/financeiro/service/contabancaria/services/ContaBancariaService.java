package br.com.finchsolucoes.financeiro.service.contabancaria.services;


import br.com.finchsolucoes.financeiro.service.contabancaria.entities.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.repositories.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.repositories.specifications.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.validators.*;
import br.com.finchsolucoes.financeiro.service.core.enums.*;
import br.com.finchsolucoes.financeiro.service.core.handlers.*;
import br.com.finchsolucoes.financeiro.service.core.handlers.exception.*;
import br.com.finchsolucoes.financeiro.service.core.utils.*;
import jakarta.transaction.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContaBancariaService {

    private final ContaBancariaRepository contaBancariaRepository;
    private final ContaBancariaMovimentacaoRepository contaBancariaMovimentacaoRepository;


    @Transactional
    public ContaBancaria criarContaBancaria(ContaBancaria conta) {


        conta.setSaldoInicial(conta.getSaldo());
        ContaBancaria contaPersistida = contaBancariaRepository.save(conta);

        aplicarAjusteDeSaldo(contaPersistida);

        return contaPersistida;
    }


    @Transactional
    public ContaBancaria updateContaBancariaByAPI(ContaBancaria conta) {
        return contaBancariaRepository.save(conta);
    }

    @Transactional
    public ContaBancaria save(ContaBancaria conta) {
        return contaBancariaRepository.save(conta);
    }

    @Transactional
    public ContaBancaria inativarContaBancariaByAPI(ContaBancaria conta) {
        return contaBancariaRepository.save(conta);
    }

    public ContaBancaria findById(Long id) {
        return this.findContaBancariaById(id);
    }

    public Page<ContaBancaria> findPageContaBancariaByApi(
            String nome,
            Banco banco,
            String agencia,
            String numeroConta,
            String titularConta,
            SituacaoContaBancaria situacao,
            Pageable pageable
    ) {
        return contaBancariaRepository.findAll(
                ContaBancariaSpecification.filter(
                        nome, banco, agencia, numeroConta, titularConta, situacao
                ),
                pageable
        );
    }

    private void validarDuplicidade(ContaBancaria contaBancaria) {
        boolean exists = false;

        if (contaBancaria.getId() == null) {
            exists = contaBancariaRepository.existsByBancoAndAgenciaAndNumeroContaAndCodigoOperacao(
                    contaBancaria.getBanco(),
                    contaBancaria.getAgencia(),
                    contaBancaria.getNumeroConta(),
                    contaBancaria.getCodigoOperacao()
            );

        } else {

            exists = contaBancariaRepository.existsByBancoAndAgenciaAndNumeroContaAndCodigoOperacaoAndIdNot(
                    contaBancaria.getBanco(),
                    contaBancaria.getAgencia(),
                    contaBancaria.getNumeroConta(),
                    contaBancaria.getCodigoOperacao(),
                    contaBancaria.getId()
            );

        }

        if (exists) {
            throw new BadRequestException(Util.retornaMensagem(MessageConstants.CONTABANCARIADUPLICADA));
        }
    }

    public void validateFields(ContaBancaria contaBancaria) {
        ContaBancariaValidator validator = new ContaBancariaValidator();
        String mensagensValidator = validator.validateFields(contaBancaria);
        if (Objects.nonNull(mensagensValidator)) {
            throw new BadRequestException(mensagensValidator);
        }
        if (contaBancaria.getBanco() == Banco.CAIXA_ECONOMICA_FEDERAL
                && (contaBancaria.getCodigoOperacao() == null
                || contaBancaria.getCodigoOperacao().isBlank())) {
            throw new BadRequestException(Util.retornaMensagem(MessageConstants.CODIGO_BANCO, ContaBancaria.class.getSimpleName()));

        }
        validarDuplicidade(contaBancaria);
    }

    public ContaBancaria findContaBancariaById(Long id) {
        return contaBancariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Util.retornaMensagem(MessageConstants.ENTITY_NOT_FOUND, ContaBancaria.class.getSimpleName(), id)));
    }

    public void validarNome(String nome) {
        contaBancariaRepository.findByNome(nome)
                .ifPresent(existing -> {
                    throw new BadRequestException(
                            Util.retornaMensagem(MessageConstants.CONTABANCARIAENTITY_ALREADY_EXISTS, nome)
                    );
                });
    }

    private void aplicarAjusteDeSaldo(ContaBancaria contaPersistida) {
        if (contaPersistida.getSaldo() != null &&
                contaPersistida.getSaldo().compareTo(BigDecimal.ZERO) != 0) {

            ContaBancariaMovimentacao ajuste = ContaBancariaMovimentacao.builder()
                    .contaBancaria(contaPersistida)
                    .valor(contaPersistida.getSaldo().abs())
                    .descricao(MessageConstants.AJUSTESALDO)
                    .operacao(contaPersistida.getSaldo().compareTo(BigDecimal.ZERO) > 0
                            ? EnumTipoOperacao.CREDITO
                            : br.com.finchsolucoes.financeiro.service.core.enums.EnumTipoOperacao.DEBITO)
                    .lancamentoFuturo(false)
                    .identificadorExterno(UUID.randomUUID().toString())
                    .responsavel(contaPersistida.getResponsavelInclusao())
                    .build();

            contaBancariaMovimentacaoRepository.save(ajuste);
        }
    }


    public Optional<ContaBancaria> findByNome(String nome) {
        return contaBancariaRepository.findByNomeIgnoreCase(nome);
    }

    public void validateNomeExistenteByUpdate(String nome, Long id) {
        Optional<ContaBancaria> outraContaComMesmoNome = this.findByNome(nome);
        if (outraContaComMesmoNome.isPresent() && !outraContaComMesmoNome.get().getId().equals(id)) {
            throw new BadRequestException(Util.retornaMensagem(MessageConstants.CONTABANCARIAENTITY_ALREADY_EXISTS, nome));
        }
    }

    public BigDecimal saldoContas() {
        return contaBancariaRepository.saldoContas();
    }
}




