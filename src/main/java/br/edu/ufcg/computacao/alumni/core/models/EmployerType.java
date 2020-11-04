package br.edu.ufcg.computacao.alumni.core.models;

public enum EmployerType {
    ACADEMY("academy"),
    INDUSTRY("industry"),
    GOVERNMENT("government"),
    ONG("ong"),
    OTHERS("others"),
    UNDEFINED("undefined");

    private String value;

    EmployerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
