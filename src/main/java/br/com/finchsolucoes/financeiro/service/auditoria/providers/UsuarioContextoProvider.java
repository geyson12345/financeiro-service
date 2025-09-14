package br.com.finchsolucoes.financeiro.service.auditoria.providers;

public class UsuarioContextoProvider {

    private static final ThreadLocal<String> USUARIO_ATUAL = new ThreadLocal<>();

    public static void definirUsuario(String usuario) {
        USUARIO_ATUAL.set(usuario);
    }

    public static String getUsuario() {
        return USUARIO_ATUAL.get();
    }

    public static void limpar() {
        USUARIO_ATUAL.remove();
    }

}