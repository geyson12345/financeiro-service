package br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs;


import br.com.finchsolucoes.financeiro.service.contabancaria.entities.*;
import br.com.finchsolucoes.financeiro.service.core.enums.*;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import java.math.*;
import java.time.*;
import lombok.*;

@Schema(name = "ContaBancariaMovimentacaoCreate", description = "Payload de entrada para criar uma movimentação de conta bancária")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContaBancariaMovimentacaoCreate(

        @Schema(description = "ID da conta bancária", example = "1")
        @NotNull
        Long contaBancariaId,

        @Schema(description = "Identificador externo da movimentação", example = "MOV12345")
        String identificadorExterno,

        @Schema(description = "Operação (CREDITO/DEBITO)", example = "CREDITO/DEBITO")
        @NotNull
        EnumTipoOperacao operacao,

        @Schema(description = "Data da movimentação", example = "2025-08-24T10:30:00")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataOperacao,

        @Schema(description = "Descrição da movimentação", example = "Pagamento fornecedor")
        @NotBlank
        String descricao,

        @Schema(description = "Valor da movimentação", example = "1000.50")
        @NotNull
        BigDecimal valor,

        @Schema(description = "Indica se é lançamento futuro", example = "false")
        @NotNull
        Boolean lancamentoFuturo
) {

    public static ContaBancariaMovimentacaoCreate newRecord(
            Long contaBancariaId,
            String identificadorExterno,
            EnumTipoOperacao operacao,
            LocalDateTime dataOperacao,
            String descricao,
            BigDecimal valor,
            Boolean lancamentoFuturo
    ) {
        return ContaBancariaMovimentacaoCreate.builder()
                .contaBancariaId(contaBancariaId)
                .identificadorExterno(identificadorExterno)
                .operacao(operacao)
                .dataOperacao(dataOperacao)
                .descricao(descricao)
                .valor(valor)
                .lancamentoFuturo(lancamentoFuturo)
                .build();
    }

    public static ContaBancariaMovimentacao convertRecordToEntity(ContaBancariaMovimentacaoCreate dto, ContaBancaria conta) {
        return ContaBancariaMovimentacao.builder()
                .contaBancaria(conta)
                .identificadorExterno(dto.identificadorExterno())
                .operacao(dto.operacao())
                .dataOperacao(dto.dataOperacao())
                .descricao(dto.descricao())
                .valor(dto.valor())
                .lancamentoFuturo(dto.lancamentoFuturo())
                .build();
    }


}

