package br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Schema(name = "ContaBancariaListOutput", description = "Payload de saída com os dados resumidos da conta bancária")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContaBancariaListOutput(

        @Schema(description = "ID da conta bancária", example = "1")
        @JsonProperty("id")
        Long id,

        @Schema(description = "UUID do sistema distribuído", example = "123e4567-e89b-12d3-a456-426614174000")
        @JsonProperty("uuid_distribuited_system")
        String uuidDistribuitedSystem,

        @Schema(description = "Nome da conta bancária", example = "Conta Empresa")
        @JsonProperty("nome")
        String nome,

        @Schema(description = "Código do banco", example = "104")
        @JsonProperty("banco")
        String banco,

        @Schema(description = "Agência da conta", example = "0001")
        @JsonProperty("agencia")
        String agencia,

        @Schema(description = "Número da conta", example = "12345")
        @JsonProperty("numero_conta")
        String numeroConta,

        @Schema(description = "Titular da conta", example = "Empresa LTDA")
        @JsonProperty("titular_conta")
        String titularConta,

        @Schema(description = "Situação da conta", example = "ATIVA")
        @JsonProperty("situacao")
        String situacao,

        @Schema(description = "Saldo atual da conta", example = "1000.50")
        @JsonProperty("saldo")
        BigDecimal saldo


) {
    public static ContaBancariaListOutput convertEntityToRecord(ContaBancaria entity) {
        return ContaBancariaListOutput.builder()
                .id(entity.getId())
                .uuidDistribuitedSystem(entity.getUuidDistribuitedSystem())
                .nome(entity.getNome())
                .banco(entity.getBanco() != null ? entity.getBanco().getNome() : null)
                .agencia(entity.getAgencia())
                .numeroConta(entity.getNumeroConta())
                .titularConta(entity.getTitularConta())
                .situacao(entity.getSituacao() != null ? entity.getSituacao().name() : null)
                .saldo(entity.getSaldo())
                .build();


    }

}