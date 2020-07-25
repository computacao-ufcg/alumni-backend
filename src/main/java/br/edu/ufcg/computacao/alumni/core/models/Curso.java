package br.edu.ufcg.computacao.alumni.core.models;

public enum Curso {
    GRADUACAO_PROCESSAMENTO_DE_DADOS("graduacao-processamento-de-dados"),
    GRADUACAO_CIENCIA_DA_COMPUTACAO("graduacao-ciencia-da-computacao"),
    MESTRADO_EM_INFOMATICA("mestrado-informatica"),
    MESTRADO_EM_CIENCIA_DA_COMPUTACAO("mestrado-ciencia-da-computacao"),
    DOUTORADO_EM_CIENCIA_DA_COMPUTACAO("doutorado-ciencia-da-computacao");

    private String value;

    Curso(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
