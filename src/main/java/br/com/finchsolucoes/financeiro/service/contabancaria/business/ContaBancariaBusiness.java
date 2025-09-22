package br.com.finchsolucoes.financeiro.service.contabancaria.business;

import br.com.finchsolucoes.financeiro.service.auditoria.providers.UsuarioContextoProvider;
import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancariaMovimentacao;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.ContaBancariaCreate;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.ContaBancariaInativarCreate;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.ContaBancariaMovimentacaoCreate;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.ContaBancariaMovimentoEstornoInput;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.ContaBancariaUpdate;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.ContaBancariaExtratoOutput;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.ContaBancariaListOutput;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.ContaBancariaMovimentacaoOutput;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.ContaBancariaOutput;
import br.com.finchsolucoes.financeiro.service.contabancaria.services.ContaBancariaMovimentacaoService;
import br.com.finchsolucoes.financeiro.service.contabancaria.services.ContaBancariaService;
import br.com.finchsolucoes.financeiro.service.core.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContaBancariaBusiness {

    private final ContaBancariaService contaBancariaService;
    private final ContaBancariaMovimentacaoService contaBancariaMovimentacaoService;

    public ContaBancariaOutput criarContaBancaria(ContaBancariaCreate dto, HttpServletRequest request) {

        String responsavel = "GEYSON";
        ContaBancaria conta = ContaBancariaCreate.convertRecordToEntity(dto);
        contaBancariaService.validateFields(conta);
        contaBancariaService.validarNome(conta.getNome());
        conta.setResponsavelInclusao(responsavel);
        conta.setTitularConta("geysongomes");
        conta.setPessoaIdTitular(1L);
        UsuarioContextoProvider.definirUsuario(responsavel);
        ContaBancaria contaPersistida = contaBancariaService.criarContaBancaria(conta);
        UsuarioContextoProvider.limpar();
        return ContaBancariaOutput.convertEntityToRecord(contaPersistida);
    }

    public ContaBancariaOutput updateContaBancariaByAPI(Long id, ContaBancariaUpdate dto, HttpServletRequest request) {

        String responsavel = "GEYSON";
        ContaBancaria contaExistente = contaBancariaService.findById(id);
        this.contaBancariaService.validateNomeExistenteByUpdate(dto.nome(), id);
        ContaBancariaUpdate.convertRecordToEntityUpdate(dto, contaExistente);
        contaBancariaService.validateFields(contaExistente);
        contaExistente.setTitularConta("geysongomes");
        contaExistente.setPessoaIdTitular(1L);
        contaExistente.setResponsavelAlteracao(responsavel);
        contaExistente.setDataUltimaAtualizacao(LocalDateTime.now());
        ContaBancaria contaAtualizada = contaBancariaService.updateContaBancariaByAPI(contaExistente);
        return ContaBancariaOutput.convertEntityToRecord(contaAtualizada);
    }

    public Page<ContaBancariaListOutput> findPageContaBancariaByApi(
            String nome,
            Banco banco,
            String agencia,
            String numeroConta,
            String titularConta,
            SituacaoContaBancaria situacao,
            Pageable pageable
    ) {
        Page<ContaBancaria> contas = contaBancariaService.findPageContaBancariaByApi(
                nome, banco, agencia, numeroConta, titularConta, situacao, pageable
        );
        return new PageImpl<>(
                contas.stream()
                        .map(ContaBancariaListOutput::convertEntityToRecord)
                        .toList(),
                pageable,
                contas.getTotalElements()
        );
    }

    public ContaBancariaMovimentacaoOutput lancarMovimentoByAPI(@Valid ContaBancariaMovimentacaoCreate dto,
                                                                HttpServletRequest request
    ) {

        String responsavel = "GEYSON";
        ContaBancaria conta = contaBancariaService.findById(dto.contaBancariaId());
        ContaBancariaMovimentacao movimentacao = ContaBancariaMovimentacaoCreate.convertRecordToEntity(dto, conta);
        movimentacao.setResponsavel(responsavel);
        contaBancariaMovimentacaoService.validateFields(movimentacao);
        UsuarioContextoProvider.definirUsuario(responsavel);
        contaBancariaMovimentacaoService.lancarMovimento(movimentacao);
        UsuarioContextoProvider.limpar();
        return ContaBancariaMovimentacaoOutput.convertEntityToRecord(movimentacao);
    }

    public ContaBancariaMovimentacaoOutput estornarMovimentoByApi(ContaBancariaMovimentoEstornoInput input, HttpServletRequest request) {

        String responsavel = "GEYSON";
        return contaBancariaMovimentacaoService.estornarMovimento(input, responsavel);
    }

    public ContaBancariaOutput inativarContaBancariaByAPI(Long id, ContaBancariaInativarCreate dto, HttpServletRequest request) {
        
        String responsavel = "GEYSON";
        ContaBancaria contaExistente = contaBancariaService.findById(id);
        contaExistente.setSituacao(SituacaoContaBancaria.INATIVA);
        contaExistente.setMotivoInativacao(dto.motivo());
        contaExistente.setResponsavelAlteracao(responsavel);
        contaExistente.setDataUltimaAtualizacao(LocalDateTime.now());
        ContaBancaria contaAtualizada = contaBancariaService.inativarContaBancariaByAPI(contaExistente);
        return ContaBancariaOutput.convertEntityToRecord(contaAtualizada);
    }

    public ContaBancariaOutput findContaBancariaById(Long id) {
        ContaBancaria entity = contaBancariaService.findContaBancariaById(id);
        return ContaBancariaOutput.convertEntityToRecord(entity);
    }


    public Page<ContaBancariaExtratoOutput> consultarExtrato(
            Long contaId,
            LocalDateTime inicio,
            LocalDateTime fim,
            Boolean lancamentoFuturo,
            Pageable pageable
    ) {
        if (inicio == null) {
            inicio = LocalDateTime.now().minusDays(30);
        }
        if (fim == null) {
            fim = LocalDateTime.now();
        }


        Page<ContaBancariaMovimentacao> page = contaBancariaMovimentacaoService.consultarExtrato(
                contaId, inicio, fim, lancamentoFuturo, pageable
        );

        return page.map(ContaBancariaExtratoOutput::fromEntity);
    }

    public byte[] exportarLancamentos(Long contaId, LocalDateTime inicio, LocalDateTime fim, Boolean lancamentoFuturo) {
        List<ContaBancariaExtratoOutput> lista =
                contaBancariaMovimentacaoService.buscarLancamentosFuturos(contaId, inicio, fim, lancamentoFuturo);
        return contaBancariaMovimentacaoService.gerarPlanilha(lista);
    }


    public BigDecimal saldoContas() {
        return contaBancariaService.saldoContas();
    }
}