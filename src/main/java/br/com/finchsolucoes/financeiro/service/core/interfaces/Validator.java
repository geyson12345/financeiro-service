package br.com.finchsolucoes.financeiro.service.core.interfaces;

public interface Validator<E> {

    String validateFields(E entity);
}
