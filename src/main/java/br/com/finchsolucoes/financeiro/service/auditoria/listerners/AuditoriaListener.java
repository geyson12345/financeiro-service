package br.com.finchsolucoes.financeiro.service.auditoria.listerners;

import br.com.finchsolucoes.financeiro.service.auditoria.entities.LogAuditoria;
import br.com.finchsolucoes.financeiro.service.auditoria.providers.UsuarioContextoProvider;
import org.hibernate.envers.RevisionListener;

import java.time.LocalDateTime;

public class AuditoriaListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        LogAuditoria rev = (LogAuditoria) revisionEntity;
        String usuario = UsuarioContextoProvider.getUsuario();
        rev.setUsuario(usuario != null ? usuario : "robo");
        rev.setDataAlteracao(LocalDateTime.now());
    }
}
