package br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs;


import br.com.finchsolucoes.financeiro.service.contabancaria.entities.*;
import br.com.finchsolucoes.financeiro.service.core.enums.*;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import java.math.*;
import java.time.*;
import lombok.*;


@Schema(name = "ContaBancariaMovimentacaoOutput", description = "Payload de saída da movimentação da conta bancária")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContaBancariaMovimentacaoOutput(

        @Schema(description = "ID da movimentação", example = "1")
        @JsonProperty("id")
        Long id,

        @Schema(description = "UUID do sistema distribuído", example = "123e4567-e89b-12d3-a456-426614174000")
        @JsonProperty("uuid_distribuited_system")
        String uuidDistribuitedSystem,

        @Schema(description = "ID da conta bancária relacionada", example = "1")
        @JsonProperty("conta_bancaria_id")
        Long contaBancariaId,

        @Schema(description = "Identificador externo da movimentação", example = "MOV12345")
        @JsonProperty("identificador_externo")
        String identificadorExterno,

        @Schema(description = "Operação (CREDITO/DEBITO)", example = "CREDITO")
        @JsonProperty("operacao")
        EnumTipoOperacao operacao,

        @Schema(description = "Data/hora da operação", example = "14/05/2014 11:45:23")
        @JsonProperty("data_operacao")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataOperacao,

        @Schema(description = "Descrição da movimentação", example = "Pagamento fornecedor")
        @JsonProperty("descricao")
        String descricao,

        @Schema(description = "Valor da movimentação", example = "1000.50")
        @JsonProperty("valor")
        BigDecimal valor,

        @Schema(description = "Indica se é lançamento futuro", example = "false")
        @JsonProperty("lancamento_futuro")
        Boolean lancamentoFuturo,

        @Schema(description = "ID da despesa", example = "1")
        @JsonProperty("despesa_id")
        Long despesaId,

        @Schema(description = "ID da receita", example = "1")
        @JsonProperty("receita_id")
        Long receitaId,

        @Schema(description = "Movimentação estornada", example = "true/false")
        @JsonProperty("movimentacao_estornada")
        Boolean movimentacaoEstornada,

        @Schema(description = "Motivo do estorno", example = "Teste")
        @JsonProperty("motivo_estorno")
        String motivoEstorno,

        @Schema(description = "Responsável pelo estorno", example = "Fulano")
        @JsonProperty("responsavel_estorno")
        String responsavelEstorno,

        @Schema(description = "Data/hora do estorno", example = "14/05/2014 11:45:23")
        @JsonProperty("data_estorno")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataEstorno

) {
    public static ContaBancariaMovimentacaoOutput convertEntityToRecord(ContaBancariaMovimentacao entity) {
        return ContaBancariaMovimentacaoOutput.builder()
                .id(entity.getId())
                .uuidDistribuitedSystem(entity.getUuidDistribuitedSystem())
                .contaBancariaId(entity.getContaBancaria().getId())
                .identificadorExterno(entity.getIdentificadorExterno())
                .operacao(entity.getOperacao())
                .dataOperacao(entity.getDataOperacao())
                .descricao(entity.getDescricao())
                .valor(entity.getValor())
                .lancamentoFuturo(entity.getLancamentoFuturo())
                .movimentacaoEstornada(entity.getMovimentacaoEstornada())
                .dataEstorno(entity.getDataEstorno())
                .motivoEstorno(entity.getMotivoEstorno())
                .responsavelEstorno(entity.getResponsavelEstorno())
                .build();
    }
}



