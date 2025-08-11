package br.com.finchsolucoes.financeiro.service.contabancaria.entities;


import br.com.finchsolucoes.financeiro.service.contabancaria.enums.Banco;
import br.com.finchsolucoes.financeiro.service.contabancaria.enums.SituacaoContaBancaria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contas_bancarias", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"banco", "agencia", "numero_conta", "codigo_operacao"})
})
@Data
@EntityListeners(AuditingEntityListener.class)

public class ContaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_integracao", nullable = false, unique = true)
    private String idIntegracao;

    @NotBlank
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "banco", nullable = false)
    private Banco banco;

    @NotBlank
    @Column(name = "agencia", nullable = false)
    private String agencia;

    @NotBlank
    @Column(name = "numero_conta", nullable = false)
    private String numeroConta;

    @NotBlank
    @Column(name = "digito_conta", nullable = false)
    private String digitoConta;

    @Column(name = "codigo_operacao")
    private String codigoOperacao;

    @NotBlank
    @Column(name = "titular_conta", nullable = false)
    private String titularConta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoContaBancaria situacao;

    @NotNull
    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    // Campos de Auditoria
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;


    @PrePersist
    public void setIdIntegracaoCreate() {
        if (Objects.isNull(this.idIntegracao)) {
            this.idIntegracao = String.valueOf(UUID.randomUUID());
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContaBancaria that = (ContaBancaria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


