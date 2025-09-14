package br.com.finchsolucoes.financeiro.service.auditoria.entities;


import br.com.finchsolucoes.financeiro.service.auditoria.listerners.AuditoriaListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "LOG_AUDITORIA")
@RevisionEntity(AuditoriaListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogAuditoria implements Serializable {

    @Serial
    private static final long serialVersionUID = -5131186390635895856L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Long id;

    @RevisionTimestamp
    @Column(name = "DATA_ALTERACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataAlteracao;

    @Column(name = "USUARIO")
    private String usuario;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        LogAuditoria that = (LogAuditoria) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDataAlteracao(), that.getDataAlteracao()) && Objects.equals(getUsuario(), that.getUsuario());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getDataAlteracao());
        result = 31 * result + Objects.hashCode(getUsuario());
        return result;
    }
}