package br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(name = "ContaBancariaMovimentoEstornoInput", description = "Payload para estorno de uma movimentação bancária")
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record ContaBancariaMovimentoEstornoInput(


        @Schema(description = "Id da movimentação a ser estornada", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @JsonProperty("movimentacao_id")
        @NotNull
        Long movimentacaoId,

        @Schema(description = "Motivo do movimentoEstorno", requiredMode = Schema.RequiredMode.REQUIRED, example = "Estorno de cobrança indevida")
        @JsonProperty("motivo")
        @NotBlank
        String motivo

) {

}
