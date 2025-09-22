package br.com.finchsolucoes.financeiro.service.contabancaria.services;


import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancariaMovimentacao;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.ContaBancariaMovimentoEstornoInput;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.ContaBancariaExtratoOutput;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.ContaBancariaMovimentacaoOutput;
import br.com.finchsolucoes.financeiro.service.contabancaria.repositories.ContaBancariaMovimentacaoRepository;
import br.com.finchsolucoes.financeiro.service.contabancaria.repositories.ContaBancariaRepository;
import br.com.finchsolucoes.financeiro.service.contabancaria.repositories.specifications.ContaBancariaMovimentacaoSpecification;
import br.com.finchsolucoes.financeiro.service.contabancaria.validators.ContaBancariaMovimentacaoValidator;

import br.com.finchsolucoes.financeiro.service.core.enums.*;
import br.com.finchsolucoes.financeiro.service.core.handlers.MessageConstants;
import br.com.finchsolucoes.financeiro.service.core.handlers.exception.BadRequestException;
import br.com.finchsolucoes.financeiro.service.core.handlers.exception.EntityNotFoundException;
import br.com.finchsolucoes.financeiro.service.core.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaBancariaMovimentacaoService {

    private final ContaBancariaMovimentacaoRepository contaBancariaMovimentacaoRepository;
    private final ContaBancariaService contaBancariaService;
    private final ContaBancariaRepository contaBancariaRepository;


    @Transactional
    public ContaBancariaMovimentacao lancarMovimento(ContaBancariaMovimentacao movimentacao) {
        contaBancariaMovimentacaoRepository.save(movimentacao);
        if (Boolean.FALSE.equals(movimentacao.getLancamentoFuturo())) {
            ContaBancaria conta = movimentacao.getContaBancaria();
            if (movimentacao.getOperacao() == EnumTipoOperacao.CREDITO) {
                conta.setSaldo(conta.getSaldo().add(movimentacao.getValor()));
            } else {
                conta.setSaldo(conta.getSaldo().subtract(movimentacao.getValor()));
            }
            contaBancariaService.save(conta);
        }
        return movimentacao;
    }

    @Transactional
    public void lancarMovimentos(List<ContaBancariaMovimentacao> movimentos) {
        movimentos.forEach(this::lancarMovimento);
    }

    @Transactional
    public ContaBancariaMovimentacaoOutput estornarMovimento(ContaBancariaMovimentoEstornoInput input, String responsavel) {
        ContaBancariaMovimentacao original = buscarMovimentacaoPorId(input.movimentacaoId());
        if (Boolean.TRUE.equals(original.getLancamentoFuturo())) {
            contaBancariaMovimentacaoRepository.delete(original);
            return null;
        }
        marcarMovimentacaoComoEstornada(original, input.motivo(), responsavel);
        ContaBancariaMovimentacao estorno = criarMovimentacaoDeEstorno(original, responsavel);
        atualizarSaldoConta(original.getContaBancaria(), estorno);
        return ContaBancariaMovimentacaoOutput.convertEntityToRecord(estorno);
    }

    public void saveMovimentoSemAlteracaoSaldo(ContaBancariaMovimentacao movimento) {
        this.contaBancariaMovimentacaoRepository.save(movimento);
    }

    @Transactional
    public Page<ContaBancariaMovimentacao> consultarExtrato(
            Long contaId,
            LocalDateTime inicio,
            LocalDateTime fim,
            Boolean lancamentoFuturo,
            Pageable pageable
    ) {
        Specification<ContaBancariaMovimentacao> spec =
                ContaBancariaMovimentacaoSpecification.contaIdEquals(contaId)
                        .and(ContaBancariaMovimentacaoSpecification.dataOperacaoBetween(inicio, fim))
                        .and(ContaBancariaMovimentacaoSpecification.lancamentoFuturoEquals(lancamentoFuturo));
        return contaBancariaMovimentacaoRepository.findAll(spec, pageable);
    }

    private ContaBancariaMovimentacao buscarMovimentacaoPorId(Long movimentacaoId) {
        return contaBancariaMovimentacaoRepository.findById(movimentacaoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Util.retornaMensagem(MessageConstants.MOVIMENTACAONOTFOUND, movimentacaoId)
                ));
    }

    private void marcarMovimentacaoComoEstornada(ContaBancariaMovimentacao original, String motivo, String responsavel) {
        original.setEstornada(true);
        original.setMotivoEstorno(motivo);
        original.setResponsavelEstorno(responsavel);
        original.setDataEstorno(LocalDateTime.now());
        contaBancariaMovimentacaoRepository.save(original);
    }

    private ContaBancariaMovimentacao criarMovimentacaoDeEstorno(ContaBancariaMovimentacao original, String responsavel) {
        ContaBancariaMovimentacao estorno = new ContaBancariaMovimentacao();
        estorno.setContaBancaria(original.getContaBancaria());
        estorno.setValor(original.getValor());
        estorno.setLancamentoFuturo(false);
        estorno.setDescricao("Estorno - " + original.getDescricao());
        estorno.setIdentificadorExterno(
                original.getIdentificadorExterno() != null ? original.getIdentificadorExterno() + "_EST" : UUID.randomUUID().toString()
        );
        estorno.setOperacao(original.getOperacao() == EnumTipoOperacao.CREDITO ? EnumTipoOperacao.DEBITO : EnumTipoOperacao.CREDITO);
        estorno.setResponsavel(responsavel);
        estorno.setMovimentacaoEstornada(true);
        estorno.setEstornada(true);
        estorno.setDataEstorno(LocalDateTime.now());
        return contaBancariaMovimentacaoRepository.save(estorno);
    }

    private void atualizarSaldoConta(ContaBancaria conta, ContaBancariaMovimentacao estorno) {
        if (estorno.getOperacao() == EnumTipoOperacao.CREDITO) {
            conta.setSaldo(conta.getSaldo().add(estorno.getValor()));
        } else {
            conta.setSaldo(conta.getSaldo().subtract(estorno.getValor()));
        }
        contaBancariaRepository.save(conta);
    }

    public void validateFields(ContaBancariaMovimentacao movimentacao) {
        ContaBancariaMovimentacaoValidator validator = new ContaBancariaMovimentacaoValidator();
        String mensagensValidator = validator.validateFields(movimentacao);
        if (Objects.nonNull(mensagensValidator)) {
            throw new BadRequestException(mensagensValidator);
        }
    }

    @Transactional
    public List<ContaBancariaExtratoOutput> buscarLancamentosFuturos(
            Long contaId,
            LocalDateTime inicio,
            LocalDateTime fim,
            Boolean lancamentoFuturo
    ) {
        if (inicio == null) {
            inicio = LocalDateTime.now().minusDays(30);
        }
        if (fim == null) {
            fim = LocalDateTime.now();
        }
        Specification<ContaBancariaMovimentacao> spec =
                ContaBancariaMovimentacaoSpecification.contaIdEquals(contaId)
                        .and(ContaBancariaMovimentacaoSpecification.dataOperacaoBetween(inicio, fim))
                        .and(ContaBancariaMovimentacaoSpecification.lancamentoFuturoEquals(lancamentoFuturo));
        List<ContaBancariaMovimentacao> movimentacoes = contaBancariaMovimentacaoRepository.findAll(spec);
        return movimentacoes.stream()
                .map(ContaBancariaExtratoOutput::fromEntity)
                .toList();
    }


    public byte[] gerarPlanilha(List<ContaBancariaExtratoOutput> lista) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Lançamentos Futuros");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Data Movimentação");
            header.createCell(1).setCellValue("Descrição");
            header.createCell(2).setCellValue("Valor");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            int rowIdx = 1;
            for (ContaBancariaExtratoOutput item : lista) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.dataMovimentacao().format(dtf));
                row.createCell(1).setCellValue(item.descricaoMovimentacao());
                row.createCell(2).setCellValue(decimalFormat.format(item.valorMovimentacao()));
            }
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new BadRequestException(Util.retornaMensagem(MessageConstants.EXCEL_ERRO_GERACAO_PLANLIHA_BYTES, ContaBancariaMovimentacaoService.class.getSimpleName(), e.getMessage()));
        }
    }

}
