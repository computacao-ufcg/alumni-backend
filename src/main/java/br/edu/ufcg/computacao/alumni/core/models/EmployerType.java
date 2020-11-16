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

    public static EmployerType getType(String value) {
        if (value.equals(ACADEMY.value)) {
            return ACADEMY;
        } else if (value.equals(INDUSTRY.value)) {
            return INDUSTRY;
        } else if (value.equals(GOVERNMENT.value)) {
            return GOVERNMENT;
        } else if (value.equals(ONG.value)) {
            return ONG;
        } else if (value.equals(OTHERS.value)) {
            return OTHERS;
        } else {
            return UNDEFINED;
        }
    }
}
