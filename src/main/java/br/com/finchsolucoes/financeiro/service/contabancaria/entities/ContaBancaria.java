package br.com.finchsolucoes.financeiro.service.contabancaria.entities;

import br.com.finchsolucoes.financeiro.service.contabancaria.converters.BancoConverter;
import br.com.finchsolucoes.financeiro.service.contabancaria.converters.SituacaoContaBancariaConverter;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import br.com.finchsolucoes.financeiro.service.core.utils.UUIDv7Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "CONTA_BANCARIA")
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancaria implements Serializable {

    @Serial
    private static final long serialVersionUID = 8065071351137036613L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid_distribuited_system", nullable = false, updatable = false)
    private String uuidDistribuitedSystem;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Convert(converter = BancoConverter.class)
    @Column(name = "banco", nullable = false)
    private Banco banco;

    @Column(name = "agencia", nullable = false)
    private String agencia;

    @Column(name = "numero_conta", nullable = false)
    private String numeroConta;

    @Column(name = "digito_conta", nullable = false)
    private String digitoConta;

    @Column(name = "codigo_operacao")
    private String codigoOperacao;

    @Column(name = "titular_conta", nullable = false)
    private String titularConta;

    @Column(name = "pessoa_Id_Titular", nullable = false)
    private Long pessoaIdTitular;

    @Convert(converter = SituacaoContaBancariaConverter.class)
    @Column(name = "situacao", nullable = false)
    private SituacaoContaBancaria situacao;

    @Column(name = "motivoinativacao")
    private String motivoInativacao;

    @Column(name = "saldoinicial", nullable = false)
    private BigDecimal saldoInicial;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @Column(name = "responsavel_created", nullable = false, updatable = false)
    private String responsavelInclusao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "responsavel_updated")
    private String responsavelAlteracao;

    @Column(name = "data_ultima_atualizacao", nullable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.dataUltimaAtualizacao = this.dataCriacao;
        this.uuidDistribuitedSystem = UUIDv7Utils.generateTimeBasedUUID().toString();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContaBancaria that)) return false;
        return Objects.equals(id, that.id) &&
                Objects.equals(uuidDistribuitedSystem, that.uuidDistribuitedSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuidDistribuitedSystem);
    }

}


