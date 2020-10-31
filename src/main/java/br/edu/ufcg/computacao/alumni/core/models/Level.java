package br.edu.ufcg.computacao.alumni.core.models;

public enum Level {
    UNDERGRADUATE("undergraduate"),
    MASTER("master"),
    DOCTORATE("doctorate");

    private String value;

    Level(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
