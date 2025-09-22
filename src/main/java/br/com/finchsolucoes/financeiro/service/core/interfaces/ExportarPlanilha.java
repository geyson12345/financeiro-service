package br.com.finchsolucoes.financeiro.service.core.interfaces;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExportarPlanilha<E> {

    byte[] gerarPlanilha(List<E> lista);

    void montarCabecalho(Sheet sheet);

    void createLines(Sheet sheet, int rowNum, E entity);

}
