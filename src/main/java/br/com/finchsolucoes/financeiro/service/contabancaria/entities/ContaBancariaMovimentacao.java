package br.com.finchsolucoes.financeiro.service.contabancaria.entities;


import br.com.finchsolucoes.financeiro.service.core.converters.*;
import br.com.finchsolucoes.financeiro.service.core.enums.*;
import br.com.finchsolucoes.financeiro.service.core.utils.*;
import jakarta.persistence.*;
import java.io.*;
import java.math.*;
import java.time.*;
import lombok.*;
import org.hibernate.envers.*;

@Entity
@Table(name = "CONTA_BANCARIA_MOVIMENTACAO")
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancariaMovimentacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1319870901023714607L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid_distribuited_system", nullable = false)
    private String uuidDistribuitedSystem;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "responsavel_movimentacao", nullable = false, updatable = false)
    private String responsavel;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_bancaria_id", nullable = false)
    private ContaBancaria contaBancaria;

    @Column(name = "motivo_estorno")
    private String motivoEstorno;

    @Column(name = "estornada")
    private Boolean estornada;

    @Column(name = "movimentacao_estornada")
    private Boolean movimentacaoEstornada;

    @Column(name = "responsavel_estorno")
    private String responsavelEstorno;

    @Column(name = "data_estorno")
    private LocalDateTime dataEstorno;

    @Convert(converter = EnumTipoOperacaoConverter.class)
    @Column(name = "operacao", nullable = false)
    private EnumTipoOperacao operacao;

    @Column(name = "data_operacao", nullable = false)
    private LocalDateTime dataOperacao;

    @Column(name = "lancamento_futuro", nullable = false)
    private Boolean lancamentoFuturo;

    @Column(name = "identificador_externo", nullable = false)
    private String identificadorExterno;


    @PrePersist
    public void setDataUuid() {
        this.dataOperacao = LocalDateTime.now();
        this.uuidDistribuitedSystem = UUIDv7Utils.generateTimeBasedUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ContaBancariaMovimentacao that = (ContaBancariaMovimentacao) o;
        return getId().equals(that.getId()) && getUuidDistribuitedSystem().equals(that.getUuidDistribuitedSystem()) && getResponsavel().equals(that.getResponsavel()) && getDescricao().equals(that.getDescricao()) && getContaBancaria().equals(that.getContaBancaria()) && getOperacao() == that.getOperacao() && getDataOperacao().equals(that.getDataOperacao()) && getLancamentoFuturo().equals(that.getLancamentoFuturo()) && getIdentificadorExterno().equals(that.getIdentificadorExterno());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUuidDistribuitedSystem().hashCode();
        result = 31 * result + getResponsavel().hashCode();
        result = 31 * result + getDescricao().hashCode();
        result = 31 * result + getContaBancaria().hashCode();
        result = 31 * result + getOperacao().hashCode();
        result = 31 * result + getDataOperacao().hashCode();
        result = 31 * result + getLancamentoFuturo().hashCode();
        result = 31 * result + getIdentificadorExterno().hashCode();
        return result;
    }

}



