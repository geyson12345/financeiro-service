package br.com.finchsolucoes.financeiro.service.contabancaria.records.outputs;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "ContaBancariaOutput", description = "Payload de saída com os dados da conta bancária")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContaBancariaOutput(

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
        Banco banco,

        @Schema(description = "Agência da conta", example = "0001")
        @JsonProperty("agencia")
        String agencia,

        @Schema(description = "Número da conta", example = "12345")
        @JsonProperty("numero_conta")
        String numeroConta,

        @Schema(description = "Dígito da conta", example = "6")
        @JsonProperty("digito_conta")
        String digitoConta,

        @Schema(description = "Código da operação", example = "001")
        @JsonProperty("codigo_operacao")
        String codigoOperacao,

        @Schema(description = "ID da pessoa titular da conta", example = "1")
        @JsonProperty("pessoa_Id_Titular")
        Long pessoaIdTitular,

        @Schema(description = "Titular da conta", example = "Empresa LTDA")
        @JsonProperty("titular_conta")
        String titularConta,

        @Schema(description = "Situação da conta", example = "ATIVA")
        @JsonProperty("situacao")
        SituacaoContaBancaria situacao,

        @Schema(description = "Saldo inicial da conta", example = "10000.50")
        @JsonProperty("saldo_inicial")
        BigDecimal saldoInicial,

        @Schema(description = "Saldo atual da conta", example = "10000.50")
        @JsonProperty("saldo")
        BigDecimal saldo,

        @Schema(description = "Responsável pela criação da conta", example = "João da Silva")
        @JsonProperty("responsavel_created")
        String responsavelCreated,

        @Schema(description = "Data de criação da conta")
        @JsonProperty("data_criacao")
        LocalDateTime dataCriacao,

        @Schema(description = "Responsável pela última atualização da conta", example = "Maria Souza")
        @JsonProperty("responsavel_updated")
        String responsavelUpdated,

        @Schema(description = "Data da última atualização da conta")
        @JsonProperty("data_ultima_atualizacao")
        LocalDateTime dataUltimaAtualizacao


) {

    public static ContaBancariaOutput convertEntityToRecord(ContaBancaria entity) {
        return ContaBancariaOutput.builder()
                .id(entity.getId())
                .uuidDistribuitedSystem(entity.getUuidDistribuitedSystem())
                .nome(entity.getNome())
                .banco(entity.getBanco())
                .agencia(entity.getAgencia())
                .numeroConta(entity.getNumeroConta())
                .digitoConta(entity.getDigitoConta())
                .codigoOperacao(entity.getCodigoOperacao())
                .pessoaIdTitular(entity.getPessoaIdTitular())
                .titularConta(entity.getTitularConta())
                .situacao(entity.getSituacao())
                .saldoInicial(entity.getSaldoInicial())
                .saldo(entity.getSaldo())
                .responsavelCreated(entity.getResponsavelInclusao())
                .dataCriacao(entity.getDataCriacao())
                .responsavelUpdated(entity.getResponsavelAlteracao())
                .dataUltimaAtualizacao(entity.getDataUltimaAtualizacao())
                .build();
    }

    @Schema(description = "Nome do banco", example = "Banco Safra")
    @JsonProperty("banco")
    public String getBancoNome() {
        return banco != null ? banco.getNome() : null;
    }

}

