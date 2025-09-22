package br.com.finchsolucoes.financeiro.service.contabancaria.records.inputs;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Schema(name = "ContaBancariaCreate", description = "Payload de entrada para criar uma conta bancária")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContaBancariaCreate(

        @Schema(description = "Nome da conta", example = "Conta PJ Matriz")
        @JsonProperty("nome")
        @NotBlank
        String nome,

        @Schema(
                description = "Banco da conta",
                example = "CAIXA_ECONOMICA_FEDERAL/BANCO_DO_BRASIL/BRADESCO/ITAU_UNIBANCO/SANTANDER/BANCO_SAFRA/CITIBANK/BTG_PACTUAL/NUBANK/BANCO_C6/BANCO_VOTORANTIM",
                allowableValues = {
                        "CAIXA_ECONOMICA_FEDERAL",
                        "BANCO_DO_BRASIL",
                        "BRADESCO",
                        "ITAU_UNIBANCO",
                        "SANTANDER",
                        "BANCO_SAFRA",
                        "CITIBANK",
                        "BTG_PACTUAL",
                        "NUBANK",
                        "BANCO_C6",
                        "BANCO_VOTORANTIM"
                }
        )
        @JsonProperty("banco")
        @NotNull
        Banco banco,

        @Schema(description = "Número da agência", example = "1234")
        @JsonProperty("agencia")
        @NotBlank
        String agencia,

        @Schema(description = "Número da conta", example = "987654")
        @JsonProperty("numero_conta")
        @NotBlank
        String numeroConta,

        @Schema(description = "Dígito da conta", example = "0")
        @JsonProperty("digito_conta")
        @NotBlank
        String digitoConta,

        @Schema(description = "Código de operação (ex.: Caixa 013 - Poupança)", example = "013")
        @JsonProperty("codigo_operacao")
        String codigoOperacao,

        @Schema(description = "ID da pessoa titular da conta", example = "168")
        @JsonProperty("pessoa_Id_Titular")
        @NotNull
        Long pessoaIdTitular,

        @Schema(description = "Situação da conta (ex.: ATIVA, INATIVA, BLOQUEADA)", example = "ATIVA")
        @JsonProperty("situacao")
        @NotNull
        SituacaoContaBancaria situacao,

        @Schema(description = "Saldo da conta", example = "1000.50")
        @JsonProperty("saldo")
        @NotNull
        BigDecimal saldo

) {
    public static ContaBancaria convertRecordToEntity(ContaBancariaCreate dto) {
        return ContaBancaria.builder()
                .nome(dto.nome())
                .banco(dto.banco())
                .agencia(dto.agencia())
                .numeroConta(dto.numeroConta())
                .digitoConta(dto.digitoConta())
                .codigoOperacao(dto.codigoOperacao())
                .pessoaIdTitular(dto.pessoaIdTitular())
                .situacao(dto.situacao())
                .saldo(dto.saldo())
                .build();
    }
}


