package br.com.finchsolucoes.financeiro.service.contabancaria.validators;

import br.com.finchsolucoes.financeiro.service.contabancaria.entities.ContaBancaria;
import br.com.finchsolucoes.financeiro.service.core.handlers.MessageConstants;
import br.com.finchsolucoes.financeiro.service.core.interfaces.Validator;
import br.com.finchsolucoes.financeiro.service.core.utils.Util;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContaBancariaValidator implements Validator<ContaBancaria> {

    @Override
    public String validateFields(ContaBancaria contaBancaria) {
        List<String> mensagensValidator = new ArrayList<>();
        this.checkFields(contaBancaria, mensagensValidator);
        if (!CollectionUtils.isEmpty(mensagensValidator)) {
            StringBuilder message = new StringBuilder();
            mensagensValidator.forEach(message::append);
            return message.toString();
        }
        return null;
    }

    private void checkFields(ContaBancaria contaBancaria, List<String> mensagens) {
        if (Objects.isNull(contaBancaria.getNome())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "nome", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(contaBancaria.getSituacao())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "situacao", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(contaBancaria.getSaldo())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "saldo", MessageConstants.NOTIFY_NULL));
        }
        if (Objects.isNull(contaBancaria.getPessoaIdTitular())) {
            mensagens.add(Util.retornaMensagem(MessageConstants.FIELDS_INVALIDATED, "pessoaIdTitular", MessageConstants.NOTIFY_NULL));
        }
    }
}
