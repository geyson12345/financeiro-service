package br.com.finchsolucoes.financeiro.service.contabancaria.validators;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.*;
import br.com.finchsolucoes.financeiro.service.core.handlers.*;
import br.com.finchsolucoes.financeiro.service.core.interfaces.*;
import br.com.finchsolucoes.financeiro.service.core.utils.*;
import java.util.*;
import org.apache.commons.collections4.*;

public class ContaBancariaMovimentacaoValidator implements Validator<ContaBancariaMovimentacao> {

    @Override
    public String validateFields(ContaBancariaMovimentacao entity) {
        List<String> mensagensValidator = new ArrayList<>();
        this.checkFields(entity, mensagensValidator);
        if (!CollectionUtils.isEmpty(mensagensValidator)) {
            StringBuilder message = new StringBuilder();
            mensagensValidator.forEach(message::append);
            return message.toString();
        }
        return null;
    }

    private void checkFields(ContaBancariaMovimentacao movimentacao, List<String> mensagens) {
        if (Objects.isNull(movimentacao.getOperacao())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "operacao", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(movimentacao.getDataOperacao())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "dataOperacao", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(movimentacao.getDescricao())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "descricao", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(movimentacao.getValor())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "valor", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(movimentacao.getContaBancaria())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "contaBancaria", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(movimentacao.getLancamentoFuturo())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "lancamentoFuturo", MessageConstants.NOTIFY_NULL));
        }

    }

}

