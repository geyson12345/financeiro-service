package br.com.finchsolucoes.financeiro.service.contabancaria.resources;

import br.com.finchsolucoes.financeiro.service.contabancaria.business.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs.*;
import br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs.*;
import br.com.finchsolucoes.financeiro.service.core.records.*;
import br.com.finchsolucoes.financeiro.service.core.utils.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;

import java.time.*;

import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conta-bancaria")
@RequiredArgsConstructor
@Tag(
        name = "Conta Bancária",
        description = "API do Gerenciamento de Conta Bancária"
)
public class ContaBancariaResource {

    private final ContaBancariaBusiness contaBancariaBusiness;

    // ======================= POST =======================
    @Operation(summary = "Criar uma nova Conta Bancária", description = "Cria uma nova conta bancária", tags = {"Conta Bancária"},
            responses = {@ApiResponse(responseCode = "201", description = "Conta criada com sucesso")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ContaBancariaOutput> criarContaBancaria(
            @Valid @RequestBody ContaBancariaCreate dto,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contaBancariaBusiness.criarContaBancaria(dto, request));
    }

    @Operation(summary = "Lançar Movimento Conta Bancária", description = "Lança uma movimentação para a conta bancária", tags = {"Conta Bancária"},
            responses = {@ApiResponse(responseCode = "201", description = "Movimentação da conta bancária executada com sucesso")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/movimento/lancar")
    public ResponseEntity<ContaBancariaMovimentacaoOutput> lancarMovimentoContaBancaria(
            @Valid @RequestBody ContaBancariaMovimentacaoCreate input,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contaBancariaBusiness.lancarMovimentoByAPI(input, request));
    }

    @Operation(summary = "Estornar movimentação bancária", description = "Estorna uma movimentação. Se for lançamento futuro, a movimentação é removida. Caso contrário, gera um lançamento contrário para ajustar o saldo.", tags = {"Conta Bancária"},
            responses = {@ApiResponse(responseCode = "201", description = "Movimento estornado com sucesso")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/movimento/estornar")
    public ResponseEntity<ContaBancariaMovimentacaoOutput> estornarMovimento(
            @Valid @RequestBody ContaBancariaMovimentoEstornoInput input,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contaBancariaBusiness.estornarMovimentoByApi(input, request));
    }

    // ======================= GET =======================
    @Operation(summary = "Consultar contas bancárias (paginação/filtros)", description = "Retorna uma página de contas bancárias filtradas pelos parâmetros informados. Todos os parâmetros são opcionais.", tags = {"Conta Bancária"},
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Consulta retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResult.class, subTypes = {ContaBancariaListOutput.class}))
            )})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/lista/page")
    public ResponseEntity<PageResult<ContaBancariaListOutput>> findPageContaBancariaByApi(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Banco banco,
            @RequestParam(required = false) String agencia,
            @RequestParam(required = false, name = "numero_conta") String numeroConta,
            @RequestParam(required = false, name = "titular_conta") String titularConta,
            @RequestParam(required = false) SituacaoContaBancaria situacao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        Pageable pageable = Util.getPageable(page, size, sortField, sortOrder);
        Page<ContaBancariaListOutput> resultado = contaBancariaBusiness.findPageContaBancariaByApi(
                nome, banco, agencia, numeroConta, titularConta, situacao, pageable
        );
        return ResponseEntity.ok(PageResult.from(resultado));
    }

    @Operation(summary = "Buscar conta bancária por ID", description = "Retorna os dados de uma conta bancária pelo seu ID", tags = {"Conta Bancária"},
            responses = {@ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso")})
    @GetMapping("/{id}/pesquisar")
    public ResponseEntity<ContaBancariaOutput> findContaBancariaById(@PathVariable Long id) {
        return ResponseEntity.ok(contaBancariaBusiness.findContaBancariaById(id));
    }

    @Operation(summary = "Consultar extrato da conta bancária", description = "Retorna uma página de movimentações bancárias dentro do período informado. Inclui lançamentos estornados e futuros (se solicitado). Por padrão, retorna os últimos 30 dias e as 10 movimentações mais recentes.", tags = {"Conta Bancária"},
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Extrato retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResult.class, subTypes = {ContaBancariaExtratoOutput.class}))
            )})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/extrato")
    public ResponseEntity<PageResult<ContaBancariaExtratoOutput>> consultarExtrato(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(name = "lancamento_futuro", required = false) Boolean lancamentoFuturo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = Util.getPageable(page, size, "dataOperacao", "DESC");
        Page<ContaBancariaExtratoOutput> resultado =
                contaBancariaBusiness.consultarExtrato(id, inicio, fim, lancamentoFuturo, pageable);

        return ResponseEntity.ok(PageResult.from(resultado));
    }

    @Operation(summary = "Exportar lançamentos futuros (Excel)", description = "Gera uma planilha Excel (.xlsx) com os lançamentos da conta bancária informada (com filtros iguais ao extrato).", tags = {"Conta Bancária"},
            responses = {@ApiResponse(responseCode = "200", description = "Arquivo gerado com sucesso")})
    @GetMapping("/{id}/relatorio/lancamentos-futuros")
    public ResponseEntity<byte[]> exportarLancamentosFuturos(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(name = "lancamento_futuro", required = false) Boolean lancamentoFuturo) {

        byte[] arquivo = contaBancariaBusiness.exportarLancamentos(id, inicio, fim, lancamentoFuturo);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=lancamentos_futuros.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(arquivo);
    }

    // ======================= PUT =======================
    @Operation(summary = "Atualizar Conta Bancária", description = "Atualiza os dados de uma conta bancária existente (exceto saldo e nome, que não podem ser alterados).", tags = {"Conta Bancária"},
            responses = {@ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso")})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<ContaBancariaOutput> updateContaBancaria(
            @PathVariable Long id,
            @Valid @RequestBody ContaBancariaUpdate dto,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contaBancariaBusiness.updateContaBancariaByAPI(id, dto, request));
    }

    @Operation(
            summary = "Inativar Conta Bancária",
            description = "Inativa uma conta bancária existente (não poderá mais ser movimentada).",
            tags = {"Conta Bancária"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta inativada com sucesso")
            }
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/inativar")
    public ResponseEntity<ContaBancariaOutput> inativarContaBancaria(
            @PathVariable Long id,
            @Valid @RequestBody ContaBancariaInativarCreate dto,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(contaBancariaBusiness.inativarContaBancariaByAPI(id, dto, request));
    }
}