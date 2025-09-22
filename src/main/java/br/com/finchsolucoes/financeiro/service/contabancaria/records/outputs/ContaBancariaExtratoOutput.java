package br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs;


import br.com.finchsolucoes.financeiro.service.contabancaria.entities.*;
import br.com.finchsolucoes.financeiro.service.core.enums.*;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.*;
import java.math.*;
import java.time.*;
import lombok.*;

@Schema(name = "ContaBancariaExtratoOutput", description = "Payload de saída do extrato de movimentações bancárias")
@Builder
public record ContaBancariaExtratoOutput(

        @Schema(description = "Data da movimentação", example = "2025-08-28T14:30:00")
        @JsonProperty("data_movimentacao")
        LocalDateTime dataMovimentacao,

        @Schema(description = "Descrição da movimentação", example = "Pagamento de boleto")
        @JsonProperty("descricao_movimentacao")
        String descricaoMovimentacao,

        @Schema(description = "Valor da movimentação (sempre positivo; operação indica débito/crédito)", example = "1500.00")
        @JsonProperty("valor_movimentacao")
        BigDecimal valorMovimentacao,

        @Schema(description = "Tipo Operação", example = "CREDITO/DEBITO"
        )
        @JsonProperty("operacao")
        EnumTipoOperacao operacao

) {

    public static ContaBancariaExtratoOutput fromEntity(ContaBancariaMovimentacao entity) {
        return ContaBancariaExtratoOutput.builder()
                .dataMovimentacao(entity.getDataOperacao())
                .descricaoMovimentacao(entity.getDescricao())
                .valorMovimentacao(entity.getValor())
                .operacao(entity.getOperacao())
                .build();
    }
}

