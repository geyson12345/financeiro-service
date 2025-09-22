package br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(name = "ContaBancariaInativarCreate", description = "Payload para inativar uma conta bancária")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContaBancariaInativarCreate(

        @Schema(description = "Motivo da inativação", example = "Encerramento solicitado pelo cliente")
        @JsonProperty("motivo")
        String motivo
) {
}
